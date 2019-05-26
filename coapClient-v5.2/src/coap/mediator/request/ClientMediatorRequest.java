package coap.mediator.request;

public abstract class ClientMediatorRequest {
	private String requestType;
	private String requestBody;
	
	public ClientMediatorRequest(String requestType, String requestBody){
		this.requestType = requestType;
		this.requestBody = requestBody;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public String getRequestBody() {
		return requestBody;
	}

	@Override
	public String toString(){
		return requestType + " - " + requestBody;
	}
}
