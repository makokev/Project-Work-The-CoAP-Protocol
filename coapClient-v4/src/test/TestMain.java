package test;

import java.io.IOException;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import coap.mediator.*;
import it.unibo.radar.coap.RadarPoint;

public class TestMain {

	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	
	public static void main(String[] args) throws IOException {
		
		CoapMediatorResponse response = null;
		CoapMediator mediator = CoapMediator.GetInstance();
		
		// Sending GET1
		CoapRequestID id1 = mediator.Get(URI_STRING);
		
		// Sending PUT2
		RadarPoint point = new RadarPoint(50,200);
		CoapRequestID id2 = mediator.Put(URI_STRING, point.compactToString(), MediaTypeRegistry.TEXT_PLAIN);
		
		// Sending GET3
		CoapRequestID id3 = mediator.Get(URI_STRING);
		
		// Reading GET3
		do {
			response = mediator.GetResponse(id3);
		} while(response == null);
		if(response.isValid()){
			point = RadarPoint.convertFromString(response.getResponse().getResponseText());
			System.out.println("Get3: "+point.toString());
		} else
			System.out.println("Get3: NO VALID RESPONSE!");
		
		// Reading PUT2
		do {
			response = mediator.GetResponse(id2);
		} while(response == null);
		
		if(response.isValid())
			System.out.println("Put2: Success!");
		else
			System.out.println("Put2: Failed!");
		
		// Reading GET1
		do {
			response = mediator.GetResponse(id1);
		} while(response == null); // no response available yet
		
		point = RadarPoint.convertFromString(response.getResponse().getResponseText());
		System.out.println("Get1: "+point.toString());
		
		// Re-reading GET1
		do {
			response = mediator.GetResponse(id1);
		} while(response == null); // no response available yet
		
		if(!response.isValid())
			System.out.println("Get1 re-read failure: test passed!\n");
		else
			System.out.println("Get1 re-read succeded: test NOT passed!\n");
		
		System.out.println("\n--- END ---");
		System.exit(0);
	}

}
