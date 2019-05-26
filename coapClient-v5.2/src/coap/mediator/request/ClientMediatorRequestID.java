package coap.mediator.request;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.coap.CoAP.Code;

// Class that represents an identifier for CoapRequest (numeric id + URI)
public class ClientMediatorRequestID {

	private Code requestType;
	private int id;
	private URI uri;
	
	public ClientMediatorRequestID(Code requestType, int id, String uri){
		this.requestType = requestType;
		this.id = id;
		try {
			this.uri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("URI not well formatted.");
		}
	}

	public Code getRequestType() {
		return requestType;
	}

	public int getNumericId() {
		return id;
	}

	public URI getUri() {
		return uri;
	}
	
	@Override
	public boolean equals(Object obj) {		
		return (obj instanceof ClientMediatorRequestID) ? this.equals((ClientMediatorRequestID) obj) : false;
	}
	
	public boolean equals(ClientMediatorRequestID request) {
		return 	this.requestType == request.getRequestType() &&
				this.getNumericId() == request.getNumericId() &&
				this.getUri().toString().compareTo(request.getUri().toString()) == 0;
	}
	
	@Override
	public String toString(){
		return requestType + " - " + getNumericId() + " - " + getUri().toString();
	}
}
