package coap.mediator.request;

import org.eclipse.californium.core.coap.CoAP.Code;

//The specific class for PUT requests
public class CoapRequestPut extends CoapRequest {

	private String payload;		// the data
	private int payloadFormat;	// data format (usually is: MediaTypeRegistry.TEXT_PLAIN)
	
	public CoapRequestPut(int id, String uri, String payload, int payloadFormat) {
		super(Code.PUT, id, uri);
		this.payload = payload;
		this.payloadFormat = payloadFormat;
	}

	public String getPayload() {
		return payload;
	}

	public int getPayloadFormat() {
		return payloadFormat;
	}
}
