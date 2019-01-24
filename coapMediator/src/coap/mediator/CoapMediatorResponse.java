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
		return this.responseCode != CoapMediatorResponseCode.ILLEGAL_REQUEST;
	}
	
	public boolean isAvailable() {
		return this.responseCode != CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET;
	}
	
	public boolean isSuccess(){
		return this.responseCode == CoapMediatorResponseCode.RESPONSE_SUCCESS;
	}
	
	public CoapMediatorResponseCode getResponseCode(){
		return responseCode;
	}
	
	public enum CoapMediatorResponseCode {
		RESPONSE_NOT_AVAILABLE_YET("Response not available yet"),
		RESPONSE_SUCCESS("Response success"),
		RESPONSE_ALREADY_READ("Response already read"),
		ILLEGAL_REQUEST("Wrong response identifier.");
		
		private String desc;
		
		CoapMediatorResponseCode(String desc) {
			this.desc=desc;
		}
		
		@Override
	    public String toString() {
			return desc;
	    }
	}
 
}