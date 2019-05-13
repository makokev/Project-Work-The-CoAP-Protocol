package main;

import java.io.*;
import java.net.*;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import coap.mediator.request.ClientMediatorRequest;
import coap.mediator.thread.*;

public class MediatorMain {

	public static final int LOCAL_MEDIATOR_PORT = 5633;

	
	public static void main(String[] args) throws IOException {
		
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(LOCAL_MEDIATOR_PORT);
		
		while (true) {
			System.out.println("Mediator: wait for connection..");
			Socket connectionSocket = welcomeSocket.accept();
			DataInputStream inFromClient = new DataInputStream((connectionSocket.getInputStream()));
			String clientMessage = inFromClient.readUTF();
			System.out.println("Message received: '"+clientMessage+"'");
			ClientMediatorRequest receivedRequest = (new Gson()).fromJson(clientMessage, ClientMediatorRequest.class);
			System.out.println("Request type: '"+receivedRequest.getRequestType()+"'.");
			JsonObject requestBody = (new Gson()).fromJson(receivedRequest.getRequestBody(), JsonObject.class);
			String uri, payload;
			int payloadFormat, requestId, requestType;
			
			switch(receivedRequest.getRequestType()){
				case "GET" :
					// message format:	GET-uri

					uri = requestBody.get("uri").getAsString();
					System.out.println("GetRequest received..");
					GetMediatorThread threadGet = new GetMediatorThread(connectionSocket, uri);
					threadGet.start();
					break;
					
				case "PUT" :
					// message format:	PUT-uri_payload_payloadFormat
					
					uri = requestBody.get("uri").getAsString();
					payload = requestBody.get("payload").getAsString();
					payloadFormat = requestBody.get("payloadFormat").getAsInt();
					System.out.println("PutRequest received..");
					
					PutMediatorThread threadPut = new PutMediatorThread(connectionSocket, uri, payload, payloadFormat);
					threadPut.start();
					break;
					
				case "RESPONSE" :
					// message format:	RESPONSE-reqeustType_uri_requestId
					
					requestType = requestBody.get("requestType").getAsInt();
					uri = requestBody.get("uri").getAsString();
					requestId = requestBody.get("requestId").getAsInt();
					System.out.println("ResponseRequest received..");
					
					ResponseMediatorThread thread = new ResponseMediatorThread(connectionSocket, Code.valueOf(requestType), requestId, uri);
					thread.start();
					break;
			}
		}
	}
}