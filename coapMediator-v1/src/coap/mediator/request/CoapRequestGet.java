package coap.mediator.request;

import org.eclipse.californium.core.coap.CoAP.Code;

// The specific class for GET requests
public class CoapRequestGet extends CoapRequest {

	public CoapRequestGet(int id, String uri) {
		super(Code.GET, id, uri);
	}
	
}
