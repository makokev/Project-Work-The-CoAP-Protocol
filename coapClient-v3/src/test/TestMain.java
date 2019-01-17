package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import coap.mediator.*;
import it.unibo.radar.coap.RadarPoint;

public class TestMain {

	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	
	public static void main(String[] args) throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		
		CoapMediatorResponse response = null;
		CoapMediator mediator = CoapMediator.GetInstance();
		
		System.out.println("Press ENTER...\n");
		buff.readLine();
		
		// Sending GET1
		CoapRequestID id1 = mediator.Get(URI_STRING);

		System.out.println("Press ENTER...\n");
		buff.readLine();
		
		// Sending PUT2
		RadarPoint point = new RadarPoint(50,200);
		CoapRequestID id2 = mediator.Put(URI_STRING, point.compactToString(), MediaTypeRegistry.TEXT_PLAIN);
		
		System.out.println("Press ENTER...\n");
		buff.readLine();
		
		// Sending GET3
		CoapRequestID id3 = mediator.Get(URI_STRING);
		
		System.out.println("Press ENTER...\n");
		buff.readLine();
		
		// Reading GET3
		do {
			response = mediator.GetResponse(id3);
		} while(response == null);
		if(response.isResponseValid()){
			point = RadarPoint.convertFromString(response.getResponse().getResponseText());
			System.out.println("Get3: "+point.toString());
		} else
			System.out.println("Get3: NO VALID RESPONSE!");
		
		// Reading PUT2
		do {
			response = mediator.GetResponse(id2);
		} while(response == null);
		
		if(response.isResponseValid())
			System.out.println("Put2: Success!");
		else
			System.out.println("Put2: Failed!");
		
		// Reading GET1
		do {
			response = mediator.GetResponse(id1);
		} while(response == null); // no response available yet
		System.out.println("");
		point = RadarPoint.convertFromString(response.getResponse().getResponseText());
		System.out.println("Get1: "+point.toString());
		
		
		
		System.out.println("\n--- END ---");
		System.exit(0);
	}

}
