package coap.mediator;

import java.util.HashMap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import coap.mediator.CoapMediatorResponse.CoapMediatorResponseCode;

public class CoapMediator {
	
	private static HashMap<Integer, CoapRequest> requests = new HashMap<>();
	private static SynchronizedCounter counter = new SynchronizedCounter();
	
	// used by clients to send a GET-REQUEST
	synchronized public static CoapRequestID Get(String uri){
		CoapRequestGet coapRequest = new CoapRequestGet(counter.GetCount(), uri);
		counter.IncrementCount();
		requests.put(coapRequest.GetRequestId().getNumericId(), coapRequest);
		(new MediatorThreadGet(coapRequest)).start();
		return coapRequest.GetRequestId();
	}
	
	// used by clients to send a PUT-REQUEST
	synchronized public static CoapRequestID Put(String uri, String payload, int payloadFormat){
		CoapRequestPut coapRequest = new CoapRequestPut(counter.GetCount(), uri, payload, payloadFormat);
		counter.IncrementCount();
		requests.put(coapRequest.GetRequestId().getNumericId(), coapRequest);
		(new MediatorThreadPut(coapRequest)).start();
		return coapRequest.GetRequestId();
	}

	// used by clients to send a RESPONSE-REQUEST to obtain the response if exists
	synchronized static public CoapMediatorResponse GetResponse(CoapRequestID coapID){
		 if(coapID.getNumericId() >= counter.GetCount() || coapID.getNumericId() < 0)
			 return new CoapMediatorResponse(null, CoapMediatorResponseCode.ILLEGAL_REQUEST);
		 if(!requests.containsKey(coapID.getNumericId()))
			 return new CoapMediatorResponse(null, CoapMediatorResponseCode.RESPONSE_ALREADY_READ);
		 
		CoapRequest request = requests.get(coapID.getNumericId());
		if(!request.IsResponseReady())
			return new CoapMediatorResponse(null, CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET);
		
		requests.remove(coapID); // the response is readable only one time!
		return new CoapMediatorResponse(request.GetResponse(), CoapMediatorResponseCode.RESPONSE_SUCCESS);
	}
	
	// used by threads to update the map with the received coap response
	synchronized protected static void RegisterResponse(CoapRequest coapRequest){
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
			coapRequest.SetResponse(response);
			CoapMediator.RegisterResponse(coapRequest);
		}	
	}
}