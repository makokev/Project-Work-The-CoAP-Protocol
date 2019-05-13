package coap.server.response;

/**
 * @author kevin
 * 
 * This class contains all information that are exchanged between
 * the CoapServer and the Client/Mediator, in order to reply at
 * GET/PUT/.. requests.
 */
public class CoapServerResponse {
	private int bodyType;
	private String body;
	
	/**
	 * @author kevin
	 * <p>
	 * bodyType: represents the body format of the response,
	 * selected from the @class MediaTypeRegistry
	 * (@see org.eclipse.californium.core.coap.MediaTypeRegistry).
	 * </p>
	 * <p>
	 * body: represents the real body of the response, written
	 * in bodyType format.
	 * </p>
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
