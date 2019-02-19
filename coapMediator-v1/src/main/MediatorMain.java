package main;

import java.io.*;
import java.net.*;

import coap.mediator.thread.*;

public class MediatorMain {

	public static final int LOCAL_MEDIATOR_PORT = 5633;
	public static final String HEADER_SEPARATOR = MediatorThread.HEADER_SEPARATOR;
	public static final String ARGUMENT_SEPARATOR = MediatorThread.ARGUMENT_SEPARATOR;

	
	public static void main(String[] args) throws IOException {
		
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(LOCAL_MEDIATOR_PORT);
		
		while (true) {
			System.out.println("Mediator: wait for connection..");
			Socket connectionSocket = welcomeSocket.accept();
			DataInputStream inFromClient = new DataInputStream((connectionSocket.getInputStream()));
			String clientMessage = inFromClient.readUTF();
			System.out.println("Message received: '"+clientMessage+"'");
			String requestType = clientMessage.split(HEADER_SEPARATOR)[0];
			System.out.println("Request type: '"+requestType+"'.");
			String string, uri, payload;
			int payloadFormat, requestId;
			
			switch(requestType){
				case "GET" :
					// message format:	GET-uri
					string = clientMessage.split(HEADER_SEPARATOR)[1];
					uri = string.split(ARGUMENT_SEPARATOR)[0];
					System.out.println("GetRequest received..");
					GetMediatorThread threadGet = new GetMediatorThread(connectionSocket, uri);
					threadGet.start();
					break;
					
				case "PUT" :
					// message format:	PUT-uri_payload_payloadFormat
					string = clientMessage.split(HEADER_SEPARATOR)[1];
					uri = string.split(ARGUMENT_SEPARATOR)[0];
					payload = string.split(ARGUMENT_SEPARATOR)[1];
					payloadFormat = Integer.parseInt(string.split(ARGUMENT_SEPARATOR)[2]);
					System.out.println("PutRequest received..");
					
					PutMediatorThread threadPut = new PutMediatorThread(connectionSocket, uri, payload, payloadFormat);
					threadPut.start();
					break;
					
				case "RESPONSE" :
					// message format:	RESPONSE-uri_requestId
					string = clientMessage.split(HEADER_SEPARATOR)[1];
					uri = string.split(ARGUMENT_SEPARATOR)[0];
					requestId = Integer.parseInt(string.split(ARGUMENT_SEPARATOR)[1]);
					System.out.println("ResponseRequest received..");
					
					ResponseMediatorThread thread = new ResponseMediatorThread(connectionSocket, requestId, uri);
					thread.start();
					break;
			}
		}
	}
}