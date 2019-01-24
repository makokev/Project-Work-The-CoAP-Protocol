package it.unibo.radar.coap.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

import it.unibo.qactors.akka.QActor;
import it.unibo.radar.coap.RadarPoint;

public class coapRadarClient {
	
	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static coapRadarClient instance;
	private static CoapClient client;
	
	public static coapRadarClient getInstance(){
		if(instance == null)
			instance = new coapRadarClient();
		return instance;
	}
	
	private coapRadarClient(){
		URI uri = null; // URI parameter of the request	
		try {
			uri = new URI(URI_STRING);
		} catch (URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
			System.exit(-1);
		}	
		client = new CoapClient(uri);
	}
	
	public static RadarPoint getResourceValue(QActor actor){
		CoapResponse response = client.get();
		if (response != null) {
			RadarPoint point = RadarPoint.convertFromString(response.getResponseText());
			return point;
		} else {
			System.out.println("No response received.");
			return null;
		}	
	}
	
	public static boolean putResourceValue(QActor actor, String distance, String angle){
		return putResourceValue(new RadarPoint(Integer.parseInt(distance), Integer.parseInt(angle)));
	}
	
	private static boolean putResourceValue(RadarPoint point){
		if(point != null){
			CoapResponse response = client.put(point.compactToString(), MediaTypeRegistry.TEXT_PLAIN);
			if(response != null) {
				if(response.getCode() == ResponseCode.CHANGED){
					System.out.println("Resource's value changed.");
					return true;
				}
				else
					System.out.println("Resource's value NOT changed.");
			}
			else
				System.out.println("No response received.");
		}
		else
			System.out.println("Invalid RadarPoint");
		return false;
	}
}


