package coap.mediator.request;

import com.google.gson.JsonObject;

public class ClientMediatorRequestPut extends ClientMediatorRequest {

	public ClientMediatorRequestPut(String uri, String payload, int payloadFormat) {
		super("PUT", getJsonBody(uri, payload, payloadFormat));
	}

	private static String getJsonBody(String uri, String payload, int payloadFormat) {
		JsonObject jsonBody = new JsonObject();
		jsonBody.addProperty("uri", uri);
		jsonBody.addProperty("payload", payload);
		jsonBody.addProperty("payloadFormat", payloadFormat);
		return jsonBody.toString();
	}

}
