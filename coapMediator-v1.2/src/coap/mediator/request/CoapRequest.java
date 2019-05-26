package coap.mediator.request;
import java.net.URI;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;

// The generic class that represents a COAP request
public abstract class CoapRequest {
	
	private ClientMediatorRequestID id;
	private transient CoapResponse response;

	// ResponseReady = false 						-> response not available yet (some errors occurred)
	// ResponseReady = true and response  = null 	-> no response available
	// ResponseReady = true and response != null 	-> response available
	private boolean responseReady;
	
	protected CoapRequest(Code requestType, int id, String uri){
		this.id = new ClientMediatorRequestID(requestType, id, uri);
		responseReady = false;
		response = null;
	}
	
	public void SetResponse(CoapResponse response){
		this.response = response;
		responseReady = true;
	}
	
	public boolean IsResponseReady(){
		return responseReady;
	}
	
	public ClientMediatorRequestID GetRequestId() {
		return id;
	}

	public int getNumericId(){
		return id.getNumericId();
	}
	
	public URI GetUri() {
		return id.getUri();
	}

	public CoapResponse GetResponse() {
		return response;
	}
	
	public boolean equals(CoapRequest request){
		return this.getNumericId() == request.getNumericId() && this.GetUri().toString().equals(request.GetUri().toString());
	}

}
