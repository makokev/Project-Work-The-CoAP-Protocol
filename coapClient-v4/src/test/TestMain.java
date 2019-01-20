package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.californium.core.coap.MediaTypeRegistry;
import coap.mediator.*;
import it.unibo.radar.coap.RadarPoint;

public class TestMain {

	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static final String REMOTE_MEDIATOR_HOST = "localhost";
	private static final int REMOTE_MEDIATOR_PORT = 5633;
	private static final String HEADER_SEPARATOR = "-";
	private static final String ARGUMENT_SEPARATOR = "_";
	
	public static void main(String[] args) throws IOException {
		
		try {
			@SuppressWarnings("resource")
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
		
			
			System.out.println("- GET");
			String message = "GET"+HEADER_SEPARATOR+URI_STRING;
			System.out.println("Message --> "+message);
			outToMediator.writeUTF(message);
			System.out.println("Message sent.");
			message = inFromMediator.readUTF();
			System.out.println("Message <-- "+message);
			String type = message.split(HEADER_SEPARATOR)[0];
			CoapRequestID requestId = null;
			if(type.equals("REQUEST_ID"))
			{
				int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
				requestId = new CoapRequestID(id, URI_STRING);
				System.out.println("REQUEST_GET ID: " + requestId.getNumericId());
				clientSocket.close();
			}else {
				System.out.println("REQUEST_GET: Response error.");
				System.exit(-1);
			}

			// ------------------------------------------------------------------------------------
			
			System.out.println("- PUT");
			clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			inFromMediator = new DataInputStream(clientSocket.getInputStream());
			RadarPoint point = new RadarPoint(50,90);
			message = "PUT"+HEADER_SEPARATOR+URI_STRING+ARGUMENT_SEPARATOR+point.compactToString()+ARGUMENT_SEPARATOR+MediaTypeRegistry.TEXT_PLAIN;
			System.out.println("Message --> "+message);
			outToMediator.writeUTF(message);
			System.out.println("Message sent.");
			message = inFromMediator.readUTF();
			System.out.println("Message <-- "+message);
			type = message.split(HEADER_SEPARATOR)[0];
			if(type.equals("REQUEST_ID"))
			{
				int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
				requestId = new CoapRequestID(id, URI_STRING);
				System.out.println("REQUEST_PUT ID: " + requestId.getNumericId());
				clientSocket.close();
			}else{
				System.out.println("REQUEST_PUT: Response error.");	
				System.exit(-1);
			}
			
		// ------------------------------------------------------------------------------------
				
		System.out.println("- RESPONSE");
		clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
		outToMediator = new DataOutputStream(clientSocket.getOutputStream());
		inFromMediator = new DataInputStream(clientSocket.getInputStream());
		
		message = "RESPONSE"+HEADER_SEPARATOR+URI_STRING+ARGUMENT_SEPARATOR+requestId.getNumericId();
		System.out.println("Message --> "+message);
		outToMediator.writeUTF(message);
		System.out.println("Message sent.");
		message = inFromMediator.readUTF();
		System.out.println("Message <-- "+message);
		type = message.split(HEADER_SEPARATOR)[0];
		if(type.equals("RESPONSE"))
		{
			String result = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[0];
			String responseText = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[1];
			if(result.equals("SUCCESS"))
				System.out.println("RESPONSE_VALUE: " + responseText);
			else
				System.out.println("RESPONSE_ERROR: " + responseText);
			clientSocket.close();
		}else{
			System.out.println("REQUEST_RESPONSE: Response error.");
			System.exit(-1);
		}
		
		System.out.println("\n--- END ---");
		System.exit(0);
	
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("Socket: creation error.\n");
	}

	}
}
