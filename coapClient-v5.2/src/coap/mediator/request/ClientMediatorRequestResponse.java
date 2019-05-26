package coap.mediator.request;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.JsonObject;

public class ClientMediatorRequestResponse extends ClientMediatorRequest {

	public ClientMediatorRequestResponse(Code requestType, String uri, int requestId) {
		super("RESPONSE", getJsonBody(requestType, uri, requestId));
	}

	private static String getJsonBody(Code requestType, String uri, int requestId) {
		JsonObject jsonBody = new JsonObject();
		jsonBody.addProperty("requestType", requestType.value);
		jsonBody.addProperty("uri", uri);
		jsonBody.addProperty("requestId", requestId);
		return jsonBody.toString();
	}

}
