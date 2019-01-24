package it.unibo.radar.coap.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import it.unibo.qactors.akka.QActor;
import it.unibo.radar.RadarPoint;

public class coapRadarClientSimple {
	
	public static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static CoapClient client;	
	
	public static void initClient(QActor actor){
		URI uri = null; // URI parameter of the request	
		try {
			uri = new URI(URI_STRING);
		} catch (URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
			System.exit(-1);
		}	
		client = new CoapClient(uri);
	}
	
	public static void getResourceValue(QActor actor){
		CoapResponse response = client.get();
		if (response != null) {
			RadarPoint point = RadarPoint.convertFromString(response.getResponseText());
			actor.emit("value_event", "value("+point.getDistance()+","+point.getAngle()+")");
		} else {
			System.out.println("No response received.");
		}
		
	}
	
	public static void putResourceValue(QActor actor, String distance, String angle){
		RadarPoint point = RadarPoint.convertFromString(distance+","+angle);
		if(point != null){
			CoapResponse response = client.put(point.compactToString(), MediaTypeRegistry.TEXT_PLAIN);
			if(response != null) {
				if(response.getCode() == ResponseCode.CHANGED)
					System.out.println("Resource's value changed.");
				else
					System.out.println("Resource's value NOT changed.");
			}
			else
				System.out.println("No response received.");
		}
		else
			System.out.println("Invalid RadarPoint");
	}
}
