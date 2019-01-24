package coap.mediator;

import org.eclipse.californium.core.CoapResponse;

// Class that represents the response from the CoapMediator, it contains all the information about the COAP response
public class CoapMediatorResponse {
	private CoapResponse response;
	private CoapMediatorResponseCode responseCode;
	
	
	public CoapMediatorResponse(CoapResponse response, CoapMediatorResponseCode responseCode){
		this.response = response;
		this.responseCode = responseCode;
	}

	public CoapResponse getResponse() {
		return response;
	}

	public boolean isValid() {
		return this.responseCode != CoapMediatorResponseCode.RESPONSE_ILLEGAL;
	}
	
	public boolean isAvailable() {
		return this.responseCode == CoapMediatorResponseCode.RESPONSE_AVAILABLE;
	}
	
	public enum CoapMediatorResponseCode {
		RESPONSE_NOT_AVAILABLE_YET,
		RESPONSE_AVAILABLE,
		RESPONSE_ILLEGAL,
		RESPONSE_ALREADY_READ
	}
 
}


