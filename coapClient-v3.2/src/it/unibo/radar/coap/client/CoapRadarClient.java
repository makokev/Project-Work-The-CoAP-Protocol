package it.unibo.radar.coap.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;

import coap.server.response.CoapServerResponse;
import it.unibo.radar.RadarPoint;
/**
 * This class represents the coap radar client. It can execute GET and PUT requests.
 */
public class CoapRadarClient {
	
	public static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static CoapRadarClient instance = null;
	private CoapClient client;	
	
	public static CoapRadarClient getInstance(){
		if(null == instance)
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
	
	/**
	 * @return
	 * It returns an instance of RadarPoint received from the server.
	 */
	public RadarPoint getResourceValue(){
		CoapResponse response = client.get();
		if (response != null){
			CoapServerResponse serverResponse = (new Gson()).fromJson(response.getResponseText(), CoapServerResponse.class);
			return (new Gson()).fromJson(serverResponse.getBody(), RadarPoint.class);
		}
		System.out.println("No response received.");
		return null;
	}

	/**
	 * @param point
	 * represents the RadarPoint that must be sent to the server.
	 * @return
	 * It returns the server response text.
	 */
	public String putResourceValue(RadarPoint point){
		if(point != null){
			CoapResponse response = client.put((new Gson()).toJson(point), MediaTypeRegistry.APPLICATION_JSON);
			if(response != null) {
				System.out.println("Response: " + response.getResponseText());
				return (new Gson()).fromJson(response.getResponseText(), CoapServerResponse.class).getBody();
			}
			else{
				System.out.println("No response received.");
				return "No response received";
			}
		}
		else
			System.out.println("Invalid RadarPoint");
		return "Invalid RadarPoint";
	}

}
