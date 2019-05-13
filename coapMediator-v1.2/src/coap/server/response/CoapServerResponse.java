package coap.server.response;

public class CoapServerResponse {
	private int bodyType;
	private String body;
	
	public CoapServerResponse(int bodyType, String body){
		this.bodyType = bodyType;
		this.body = body;
	}

	public int getBodyType() {
		return bodyType;
	}

	public String getBody() {
		return body;
	}
	
	@Override
	public String toString(){
		return bodyType + " - " + body;
	}
}
