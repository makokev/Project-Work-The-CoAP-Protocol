package coap.mediator;

import org.eclipse.californium.core.CoapResponse;

// Class that represents the response from the CoapMediator, it contains all the information about the COAP response
public class CoapMediatorResponse {
	private CoapResponse response;
	private boolean responseValid;
	
	public CoapMediatorResponse(CoapResponse response, boolean responseValid){
		this.response = response;
		this.responseValid = responseValid;
	}

	public CoapResponse getResponse() {
		return response;
	}

	public boolean isResponseValid() {
		return responseValid;
	}
	
}
