package main;

import coap.mediator.CoapMediator;
import coap.mediator.CoapMediatorResponse;
import coap.mediator.CoapRequestID;

import java.io.*;
import java.net.*;

public class MediatorMain {

	public static final int LOCAL_PORT = 5633;
	public static final String HEADER_SEPARATOR = "-";
	public static final String ARGUMENT_SEPARATOR = "!";

	public static void main(String[] args) throws IOException {
		
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(LOCAL_PORT);
		
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
					GetThread threadGet = new GetThread(connectionSocket, uri);
					threadGet.start();
					break;
					
				case "PUT" :
					// message format:	PUT-uri_payload_payloadFormat
					string = clientMessage.split(HEADER_SEPARATOR)[1];
					uri = string.split(ARGUMENT_SEPARATOR)[0];
					payload = string.split(ARGUMENT_SEPARATOR)[1];
					payloadFormat = Integer.parseInt(string.split(ARGUMENT_SEPARATOR)[2]);
					System.out.println("PutRequest received..");
					
					PutThread threadPut = new PutThread(connectionSocket, uri, payload, payloadFormat);
					threadPut.start();
					break;
					
				case "RESPONSE" :
					// message format:	RESPONSE-uri_requestId
					string = clientMessage.split(HEADER_SEPARATOR)[1];
					uri = string.split(ARGUMENT_SEPARATOR)[0];
					requestId = Integer.parseInt(string.split(ARGUMENT_SEPARATOR)[1]);
					System.out.println("ResponseRequest received..");
					
					ResponseThread thread = new ResponseThread(connectionSocket, requestId, uri);
					thread.start();
					break;
			}
		}
	}
}

class GetThread extends Thread
{
	private static final String HEADER_SEPARATOR = MediatorMain.HEADER_SEPARATOR;
	private Socket clientSocket;
	private DataOutputStream out;
	private String uri;
	
	public GetThread(Socket clientSocket, String uri) throws IOException{
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.uri = uri;
	}
	
	@Override
	public void start(){
		// message format:		REQUEST_ID-id
		
		CoapRequestID id = CoapMediator.Get(uri);
		System.out.println("GetThread started.");
		String message = "REQUEST_ID"+HEADER_SEPARATOR+id.getNumericId();
		try{
			out.writeUTF(message);
			System.out.println("Message --> "+message);
			clientSocket.close();
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
		
	}
}

class PutThread extends Thread
{
	private static final String HEADER_SEPARATOR = MediatorMain.HEADER_SEPARATOR;

	private Socket clientSocket;
	private DataOutputStream out;
	private String uri;
	private String payload;
	private int payloadFormat;

	public PutThread(Socket clientSocket, String uri, String payload, int payloadFormat) throws IOException{
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.uri = uri;
		this.payload = payload;
		this.payloadFormat = payloadFormat;
	}
	
	@Override
	public void start(){
		// message format:		REQUEST_ID-id
		
		CoapRequestID id = CoapMediator.Put(uri, payload, payloadFormat);
		System.out.println("PutThread started.");
		String message = "REQUEST_ID"+HEADER_SEPARATOR+id.getNumericId();
		try{
			out.writeUTF(message);
			System.out.println("Message --> "+message);
			clientSocket.close();
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
	}
}

class ResponseThread extends Thread
{
	private static final String HEADER_SEPARATOR = MediatorMain.HEADER_SEPARATOR;
	private static final String ARGUMENT_SEPARATOR = MediatorMain.ARGUMENT_SEPARATOR;

	private Socket clientSocket;
	private DataOutputStream out;
	private int requestId;
	private String uri;
	
	public ResponseThread(Socket clientSocket, int requestId, String uri) throws IOException{
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.requestId = requestId;
		this.uri = uri;
	}
	
	@Override
	public void start(){
		// message format:		RESPONSE-responseCode_responseText
		// 									or
		// message format:		RESPONSE-FAILURE_failureCode
			
		CoapMediatorResponse response = null;
		CoapRequestID id = new CoapRequestID(requestId, uri);
		System.out.println("ResponseThread started.");
		try{
			response = CoapMediator.GetResponse(id);		
			String message = "RESPONSE"+HEADER_SEPARATOR;
			
			message += response.getResponseCode() + ARGUMENT_SEPARATOR;
			
			if(response.isValid() && response.isSuccess())
					message += response.getResponse().getResponseText();
			 else
				message += response.getResponseCode().getDescription();
			
			out.writeUTF(message);
			System.out.println("Message --> "+message);
			System.out.println("ResponseThread stopped.");
			clientSocket.close();
		} catch(Exception e){
			System.out.println("ResponseThread: out socket error.");
			e.printStackTrace();
		}
	}
}
