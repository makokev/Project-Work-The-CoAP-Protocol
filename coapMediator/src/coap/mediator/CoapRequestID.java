package coap.mediator;

import java.net.URI;
import java.net.URISyntaxException;

// Class that represents an identifier for CoapRequest (numeric id + URI)
public class CoapRequestID {

	private int id;
	private URI uri;
	
	public CoapRequestID(int id, String uri){
		this.id = id;
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("URI not well formatted.");
		}
	}

	public int getNumericId() {
		return id;
	}

	public URI getUri() {
		return uri;
	}
	
}
