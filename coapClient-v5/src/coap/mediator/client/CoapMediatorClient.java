package coap.mediator.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;

import coap.mediator.request.CoapRequestID;
import coap.mediator.response.CoapMediatorResponse;
import coap.mediator.response.CoapMediatorResponseCode;

public class CoapMediatorClient {
	
	private static final String REMOTE_MEDIATOR_HOST = "localhost";
	private static final int REMOTE_MEDIATOR_PORT = 5633;
	
	public static CoapRequestID Get(String resourceURI){
		// out format:	ClientMediatorRequestGet
		// in  format:	CoapRequestID
		
		try {
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			ClientMediatorRequestGet request = new ClientMediatorRequestGet(resourceURI);
			String message = (new Gson()).toJson(request);
			System.out.println("Message to mediator --> "+message);
			outToMediator.writeUTF(message);
			message = inFromMediator.readUTF();
			clientSocket.close();
			System.out.println("Message from mediator <-- "+message);
			CoapRequestID requestId = (new Gson()).fromJson(message, CoapRequestID.class);
			System.out.println("REQUEST_GET ID: " + requestId.getNumericId()+"\n");
			return requestId;
			
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Socket error.\n");
		}
		return null;
	}
	
	public static CoapRequestID Put(String resourceURI, String payload){
		// out format:	ClientMediatorRequestPut
		// in  format:	CoapRequestID
				
		try {
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			ClientMediatorRequestPut request = new ClientMediatorRequestPut(resourceURI, payload, MediaTypeRegistry.APPLICATION_JSON);
			String message = (new Gson()).toJson(request);
			System.out.println("Message to mediator --> "+message);
			outToMediator.writeUTF(message);
			message = inFromMediator.readUTF();
			clientSocket.close();
			System.out.println("Message from mediator <-- "+message);
			CoapRequestID requestId = (new Gson()).fromJson(message, CoapRequestID.class);
			System.out.println("REQUEST_PUT ID: " + requestId.getNumericId()+"\n");
			return requestId;
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Socket error.\n");
		}
		return null;
	}
	
	public static CoapMediatorResponse GetResponse(CoapRequestID requestId){
		// out format:	ClientMediatorRequestResponse
		// in  format:	CoapMediatorResponse
		
		try{
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			ClientMediatorRequestResponse request = new ClientMediatorRequestResponse(requestId.getRequestType(), requestId.getUri().toString(), requestId.getNumericId());
			String message = (new Gson()).toJson(request);
			System.out.println("Message to mediator --> "+message);
			outToMediator.writeUTF(message);
			
			message = inFromMediator.readUTF();
			clientSocket.close();
			
			System.out.println("Message from mediator <-- "+message);
			CoapMediatorResponse response = (new Gson()).fromJson(message, CoapMediatorResponse.class);
			return response;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Socket error.\n");
		}
		return new CoapMediatorResponse(requestId, CoapMediatorResponseCode.RESPONSE_FORMAT_ERROR, "RESPONSE_ERROR: "+CoapMediatorResponseCode.RESPONSE_FORMAT_ERROR.getDescription(), MediaTypeRegistry.TEXT_PLAIN);
	}

}