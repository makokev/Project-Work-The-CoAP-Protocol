package coap.server.response;

/**
 * This class contains all information that are sent from the server
 * to clients in order to reply at GET/PUT/.. requests.
 */
public class CoapServerResponse {
	private int bodyType;
	private String body;
	
	/**
	 * @param bodyType
	 * represents the body format of the response, selected from the class MediaTypeRegistry
	 * (usually, <b>APPLICATION_JSON</b> or <b>TEXT_PLAIN</b>).
	 * @param body
	 * represents the real body of the response, written in bodyType format.
	 * 
	 * @see
	 * {@link org.eclipse.californium.core.coap.MediaTypeRegistry}
	 * 
	 */
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
