package coap.mediator;

public enum CoapMediatorResponseCode {
	RESPONSE_NOT_AVAILABLE_YET("Response not available yet"),
	RESPONSE_SUCCESS("Response success"),
	RESPONSE_ALREADY_READ("Response already read"),
	ILLEGAL_REQUEST("Wrong response identifier."),
	RESPONSE_FORMAT_ERROR("Response format error");
	
	private String desc;
	
	CoapMediatorResponseCode(String desc) {
		this.desc=desc;
	}
	
	public String getDescription(){
		return desc;
	}
	
	public boolean isValid() {
		return 	this != CoapMediatorResponseCode.ILLEGAL_REQUEST &&
				this != CoapMediatorResponseCode.RESPONSE_FORMAT_ERROR;
	}
	
	public boolean isAvailable() {
		return this != CoapMediatorResponseCode.RESPONSE_NOT_AVAILABLE_YET;
	}
	
	public boolean isSuccess(){
		return this == CoapMediatorResponseCode.RESPONSE_SUCCESS;
	}
	
}
