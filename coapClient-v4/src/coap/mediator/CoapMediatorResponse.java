package coap.mediator;

import org.eclipse.californium.core.CoapResponse;

// Class that represents the response from the CoapMediator, it contains all the information about the COAP response
public class CoapMediatorResponse {
	private CoapResponse response;
	private boolean isValid;
	
	public CoapMediatorResponse(CoapResponse response, boolean isValid){
		this.response = response;
		this.isValid = isValid;
	}

	public CoapResponse getResponse() {
		return response;
	}

	public boolean isValid() {
		return isValid;
	}
	
}
