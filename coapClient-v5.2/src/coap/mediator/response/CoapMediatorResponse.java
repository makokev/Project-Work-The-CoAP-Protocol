package coap.mediator.response;

import coap.mediator.request.ClientMediatorRequestID;

public class CoapMediatorResponse {
	private ClientMediatorRequestID requestId;
	private CoapMediatorResponseCode responseCode;
	private String responseBody;
	private int responseBodyType;
	
	public CoapMediatorResponse(ClientMediatorRequestID requestId, CoapMediatorResponseCode responseCode, String responseBody, int responseBodyType){
		this.requestId = requestId;
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.responseBodyType = responseBodyType;
	}
	
	public ClientMediatorRequestID getRequestId() {
		return requestId;
	}

	public CoapMediatorResponseCode getResponseCode() {
		return responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}
	
	public int getResponseBodyType() {
		return responseBodyType;
	}

	@Override
	public String toString(){
		return requestId + " - " + responseCode.name() + " - " + responseBodyType + " - " + responseBody;
	}
}
