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
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

import it.unibo.qactors.akka.QActor;
import it.unibo.radar.coap.RadarPoint;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;

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
            exchange.respond(radarPoint.compactToString());
        }
    	
    	public void handlePUT(CoapExchange exange) {
    		System.out.println("PUT request received.");
    		String message = exange.getRequestText();
    		try{
    			radarPoint = RadarPoint.convertFromString(message);
    			ACTOR.emit("polar", "p("+radarPoint.compactToString()+")"); // changing radar gui
    			exange.respond(ResponseCode.CHANGED);
    			changed();
    		} catch(IllegalArgumentException e){
    			exange.respond(ResponseCode.UNSUPPORTED_CONTENT_FORMAT, "Request ignored.");
    		}
    	}
    }
    
}

