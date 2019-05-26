package coap.mediator;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;

import coap.mediator.request.*;
import coap.mediator.response.*;
import coap.server.response.CoapServerResponse;

public class CoapMediator {
	
	private static ConcurrentHashMap<Integer, CoapRequest> requests = new ConcurrentHashMap<>();
	private static SynchronisedCounter counter = new SynchronisedCounter();
	
	// used by clients to send a GET-REQUEST
	public static ClientMediatorRequestID Get(String uri){
		CoapRequestGet coapRequest = new CoapRequestGet(counter.GetCount(), uri);
		counter.IncrementCount();
		requests.put(coapRequest.GetRequestId().getNumericId(), coapRequest);
		(new MediatorThreadGet(coapRequest)).start();
		return coapRequest.GetRequestId();
	}
	
	// used by clients to send a PUT-REQUEST
	public static ClientMediatorRequestID Put(String uri, String payload, int payloadFormat){
		CoapRequestPut coapRequest = new CoapRequestPut(counter.GetCount(), uri, payload, payloadFormat);
		counter.IncrementCount();
		requests.put(coapRequest.GetRequestId().getNumericId(), coapRequest);
		(new MediatorThreadPut(coapRequest)).start();
		return coapRequest.GetRequestId();
	}

	// used by clients to send a RESPONSE-REQUEST to obtain the response if exists
	static public CoapMediatorResponse GetResponse(ClientMediatorRequestID coapID){
		 if(coapID.getNumericId() >= counter.GetCount() || coapID.getNumericId() < 0)
			 return new CoapMediatorResponse(coapID, CoapMediatorResponseCode.ILLEGAL_REQUEST, CoapMediatorResponseCode.ILLEGAL_REQUEST.getDescription(), MediaTypeRegistry.TEXT_PLAIN);
		 if(!requests.containsKey(coapID.getNumericId()))
			 return new CoapMediatorResponse(coapID, CoapMediatorResponseCode.RESPONSE_ALREADY_READ, CoapMediatorResponseCode.RESPONSE_ALREADY_READ.getDescription(), MediaTypeRegistry.TEXT_PLAIN);
		 
		CoapRequest request = requests.get(coapID.getNumericId());
		if(!request.IsResponseReady())
			return new CoapMediatorResponse(coapID, CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET, CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET.getDescription(), MediaTypeRegistry.TEXT_PLAIN);
		
		requests.remove(coapID.getNumericId()); // the response is readable only once!
		System.out.println("RESPONSE TEXT: "+request.GetResponse().getResponseText());
		
		CoapServerResponse serverResponse = (new Gson()).fromJson(request.GetResponse().getResponseText(), CoapServerResponse.class);
		return new CoapMediatorResponse(coapID, CoapMediatorResponseCode.RESPONSE_SUCCESS, serverResponse.getBody(), serverResponse.getBodyType());
	}
	
	// used by threads to update the map with the received coap response
	protected static void RegisterResponse(CoapRequest coapRequest){
		requests.put(coapRequest.GetRequestId().getNumericId(), coapRequest); // update the value of coapRequest
	}
	

	//
	// MediatorThread Classes ( BASE - GET - PUT - ... )
	//
	
	// the generic class
	public static abstract class MediatorThread extends Thread{
		protected CoapRequest coapRequest;
		
		protected MediatorThread(CoapRequest coapRequest){
			this.coapRequest = coapRequest;
		}
		
		@Override
		public abstract void run();
	}
	
	// the specific GET class
	public static class MediatorThreadGet extends MediatorThread{
		
		public MediatorThreadGet(CoapRequestGet coapRequest){
			super(coapRequest);
		}

		@Override
		public void run(){
			CoapClient client = new CoapClient(coapRequest.GetUri());
			System.out.println("GET URI: "+coapRequest.GetUri());
			CoapResponse response = client.get();
			System.out.println("Message from server <-- " + response.getResponseText());
			System.out.println("CoapRequestID: " + coapRequest.GetRequestId());
			coapRequest.SetResponse(response);
			CoapMediator.RegisterResponse(coapRequest);
		}		
	}
	
	// the specific PUT class
	public static class MediatorThreadPut extends MediatorThread{
		
		public MediatorThreadPut(CoapRequestPut coapRequest){
			super(coapRequest);
		}

		@Override
		public void run(){
			CoapClient client = new CoapClient(coapRequest.GetUri());
			CoapRequestPut putRequest = (CoapRequestPut) coapRequest;
			System.out.println("PUT URI: "+coapRequest.GetUri() + " PAYLOAD: " + putRequest.getPayload());
			CoapResponse response = client.put(putRequest.getPayload(), putRequest.getPayloadFormat());
			System.out.println("Message from server <-- " + response.getResponseText());
			System.out.println("CoapRequestID: " + coapRequest.GetRequestId());
			coapRequest.SetResponse(response);
			CoapMediator.RegisterResponse(coapRequest);
		}	
	}
}