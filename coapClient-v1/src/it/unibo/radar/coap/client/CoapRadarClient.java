package it.unibo.radar.coap.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

import it.unibo.radar.RadarPoint;

public class CoapRadarClient {
	
	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static CoapRadarClient instance;
	private CoapClient client;
	
	public static CoapRadarClient getInstance(){
		if(instance == null)
			instance = new CoapRadarClient();
		return instance;
	}
	
	private CoapRadarClient(){
		URI uri = null; // URI parameter of the request	
		try {
			uri = new URI(URI_STRING);
		} catch (URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
			System.exit(-1);
		}	
		client = new CoapClient(uri);
	}
	
	public RadarPoint getResourceValue(){
		CoapResponse response = client.get();
		if (response != null) {
			RadarPoint point = RadarPoint.convertFromString(response.getResponseText());
			return point;
		} else {
			System.out.println("No response received.");
			return null;
		}	
	}
	
	public boolean putResourceValue(RadarPoint point){
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


