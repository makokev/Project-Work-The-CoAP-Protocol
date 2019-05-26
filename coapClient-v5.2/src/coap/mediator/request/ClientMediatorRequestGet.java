package coap.mediator.request;

import com.google.gson.JsonObject;

public class ClientMediatorRequestGet extends ClientMediatorRequest {

	public ClientMediatorRequestGet(String uri) {
		super("GET", getJsonUri(uri));
		// TODO Auto-generated constructor stub
	}
	
	private static String getJsonUri(String uri){
		JsonObject jsonUri = new JsonObject();
		jsonUri.addProperty("uri", uri);
		return jsonUri.toString();
	}

}
