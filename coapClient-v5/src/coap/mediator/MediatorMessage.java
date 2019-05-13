package coap.mediator;

public class MediatorMessage {
	private CoapMediatorResponseCode responseCode;
	private String message;
	
	public MediatorMessage(CoapMediatorResponseCode code, String message){
		this.responseCode = code;
		this.message = message;
	}
	
	public boolean isValid() {
		return 	responseCode.isValid();
	}
	
	public boolean isAvailable() {
		return responseCode.isAvailable();
	}
	
	public boolean isSuccess(){
		return responseCode.isSuccess();
	}
	
	public String getMessage(){
		return message;
	}
}