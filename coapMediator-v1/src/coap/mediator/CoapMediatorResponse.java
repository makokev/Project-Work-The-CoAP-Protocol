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
		return responseCode.isValid();
	}
	
	public boolean isAvailable() {
		return responseCode.isAvailable();
	}
	
	public boolean isSuccess(){
		return responseCode.isSuccess();
	}
	
	public CoapMediatorResponseCode getResponseCode(){
		return responseCode;
	}
 
}