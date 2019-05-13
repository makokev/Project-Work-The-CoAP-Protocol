/*******************************************************************************
 * Copyright (c) 2015 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 *    Kai Hudalla (Bosch Software Innovations GmbH) - add endpoints for all IP addresses
 ******************************************************************************/

package it.unibo.radar.coap.server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;

import coap.server.response.CoapServerResponse;
import it.unibo.qactors.akka.QActor;
import it.unibo.radar.RadarPoint;

public class coapRadarServer extends CoapServer{	
	private static QActor ACTOR;
    private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
    private static Thread workingThread;
    
    private coapRadarServer() throws SocketException { add(new RadarPointResource()); }
    
	public static void startServer(QActor actor) {
		coapRadarServer.ACTOR = actor;
		
		workingThread = new Thread("Working thread") {
		      public void run(){
		    	  try {
		              coapRadarServer server = new coapRadarServer(); // create coap server
		              server.addEndpoints(); // add endpoints on all IP addresses
		              RadarPoint radarPoint = new RadarPoint();
		              ACTOR.emit("polar", "p("+radarPoint.compactToString()+")");
		              server.start();
		              System.out.println("CoapRadarServer started (port="+COAP_PORT+").");
		          } catch (SocketException e) {
		              System.err.println("Failed to initialize server: " + e.getMessage());
		          }
		      }
		   };
		workingThread.start();
    }
	
	public static void stopServer(QActor actor){
		if(workingThread != null){
			workingThread.interrupt();
			workingThread = null;
		}
	}
	
    private void addEndpoints() {
    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
    		if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    }
    
    private class RadarPointResource extends CoapResource{
    	private RadarPoint radarPoint; // The actual status of the resource (current coordinates)
    	
    	public RadarPointResource() {
    		super("RadarPoint"); // logical name of the resource
            getAttributes().setTitle("RadarPoint Resource");
            radarPoint = new RadarPoint(); // initialize at (0,0) coordinates
    	}
    	
    	@Override
        public void handleGET(CoapExchange exchange) {
    		System.out.println("GET request received.");
    		System.out.println("Message from client/mediator <-- " + exchange.getRequestText());
    		CoapServerResponse response = new CoapServerResponse(MediaTypeRegistry.APPLICATION_JSON, (new Gson()).toJson(radarPoint));
    		System.out.println("Message to client/mediator --> " + (new Gson()).toJson(response));
    		exchange.respond((new Gson()).toJson(response));
        }
    	
    	@Override
    	public void handlePUT(CoapExchange exchange) {
    		CoapServerResponse response;
    		System.out.println("PUT request received.");
    		System.out.println("Message from client/mediator <-- " + exchange.getRequestText());
    		String message = exchange.getRequestText();
    		System.out.println("message: "+message);
    		try{
    			radarPoint = (new Gson()).fromJson(message, RadarPoint.class);
    			ACTOR.emit("polar", "p("+radarPoint.compactToString()+")"); // changing radar gui
    			changed();
    			response = new CoapServerResponse(MediaTypeRegistry.TEXT_PLAIN, "Resource changed");    			
    		} catch(IllegalArgumentException e){
    			response = new CoapServerResponse(MediaTypeRegistry.TEXT_PLAIN, "Request ignored becuase content format was unsupported");
    		}
    		System.out.println("Message to client/mediator --> " + (new Gson()).toJson(response));
    		exchange.respond((new Gson()).toJson(response));
    	}
    }
    
}

