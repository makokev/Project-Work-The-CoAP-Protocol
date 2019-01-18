package coap.mediator;

import java.util.HashMap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

public class CoapMediator {
	
	private static CoapMediator instance = null;
	private static HashMap<CoapRequestID, CoapRequest> map;
	private static Counter counter;
	
	// used by clients to get an instance of the mediator
	synchronized public static CoapMediator GetInstance(){
		if(instance == null){
			instance = new CoapMediator();
			map = new HashMap<>();
			counter = new Counter(); // count = 0;
		}
		return instance;
	}
	
	// used by clients to send a GET-REQUEST
	public CoapRequestID Get(String uri){
		CoapRequestGet coapRequest = new CoapRequestGet(counter.GetCount(), uri);
		counter.IncrementCount();
		map.put(coapRequest.GetRequestId(), coapRequest);
		(instance.new MediatorThreadGet(coapRequest)).start();
		return coapRequest.GetRequestId();
	}
	
	// used by clients to send a PUT-REQUEST
	public CoapRequestID Put(String uri, String payload, int payloadFormat){
		CoapRequestPut coapRequest = new CoapRequestPut(counter.GetCount(), uri, payload, payloadFormat);
		counter.IncrementCount();
		map.put(coapRequest.GetRequestId(), coapRequest);
		(instance.new MediatorThreadPut(coapRequest)).start();
		return coapRequest.GetRequestId();
	}

	// used by clients to obtain the response if exists, otherwise a null is returned
	public CoapMediatorResponse GetResponse(CoapRequestID coapID){
		CoapRequest request = map.get(coapID);
		if(request.IsResponseReady()){
			map.remove(coapID); // the response is readable only one time!
			return new CoapMediatorResponse(request.GetResponse(), ResponseCode.isSuccess(request.GetResponse().getCode()));
		}
		else
			return null;
		
	}
	
	// used by threads to update the map with the received coap response
	synchronized protected void RegisterResponse(CoapRequest coapRequest){
		map.put(coapRequest.GetRequestId(), coapRequest); // update the value of coapRequest
	}
	

	//
	// MediatorThread Classes ( BASE - GET - PUT - ... )
	//
	
	// the generic class
	public abstract class MediatorThread extends Thread{
		protected CoapRequest coapRequest;
		
		protected MediatorThread(CoapRequest coapRequest){
			this.coapRequest = coapRequest;
		}
		
		@Override
		public abstract void run();
	}
	
	// the specific GET class
	public class MediatorThreadGet extends MediatorThread{
		
		public MediatorThreadGet(CoapRequestGet coapRequest){
			super(coapRequest);
		}

		@Override
		public void run(){
			CoapClient client = new CoapClient(coapRequest.GetUri());
			CoapResponse response = client.get();
			coapRequest.SetResponse(response);
			CoapMediator.GetInstance().RegisterResponse(coapRequest);
		}		
	}
	
	// the specific PUT class
	public class MediatorThreadPut extends MediatorThread{
		
		public MediatorThreadPut(CoapRequestPut coapRequest){
			super(coapRequest);
		}

		@Override
		public void run(){
			CoapClient client = new CoapClient(coapRequest.GetUri());
			CoapRequestPut putRequest = (CoapRequestPut) coapRequest;
			CoapResponse response = client.put(putRequest.getPayload(), putRequest.getPayloadFormat());
			coapRequest.SetResponse(response);
			CoapMediator.GetInstance().RegisterResponse(coapRequest);			
		}	
	}

}