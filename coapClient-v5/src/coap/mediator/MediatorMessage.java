package coap.mediator;

public class MediatorMessage {
	private CoapMediatorResponseCode responseCode;
	private String message;
	
	public MediatorMessage(CoapMediatorResponseCode code, String message){
		this.responseCode = code;
		this.message = message;
	}
	
	public boolean isValid() {
		return 	responseCode != CoapMediatorResponseCode.ILLEGAL_REQUEST &&
				responseCode != CoapMediatorResponseCode.RESPONSE_FORMAT_ERROR;
	}
	
	public boolean isAvailable() {
		return responseCode != CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET;
	}
	
	public boolean isSuccess(){
		return responseCode == CoapMediatorResponseCode.RESPONSE_SUCCESS;
	}
	
	public String getMessage(){
		return message;
	}
	
}
