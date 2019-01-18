package main;


import coap.mediator.CoapMediator;
import coap.mediator.CoapMediatorResponse;
import coap.mediator.CoapRequestID;

import java.io.*;
import java.net.*;

public class MediatorMain {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket welcomeSocket = new ServerSocket(5633);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			DataInputStream inFromClient = new DataInputStream((connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			String clientMessage = inFromClient.readUTF();
			String requestType = clientMessage.split(":")[0];
			String string, uri, payload;
			int payloadFormat, requestId;
			
			switch(requestType){
				case "GET" : // message format:			GET:uri
					string = clientMessage.split(":")[1];
					uri = string.split(",")[0];
					
					GetThread threadGet = new GetThread(outToClient, uri);
					threadGet.start();
					break;
					
				case "PUT" : // message format:			PUT:uri,payload,payloadFormat
					string = clientMessage.split(":")[1];
					uri = string.split(",")[0];
					payload = string.split(",")[1];
					payloadFormat = Integer.parseInt(string.split(",")[2]);
					
					PutThread threadPut = new PutThread(outToClient, uri, payload, payloadFormat);
					threadPut.start();
					break;
					
				case "RESPONSE" : // message format:	RESPONSE:uri,requestId
					string = clientMessage.split(":")[1];
					uri = string.split(",")[0];
					requestId = Integer.parseInt(string.split(",")[1]);
					
					ResponseThread thread = new ResponseThread(outToClient, requestId, uri);
					thread.start();
					break;
					
			}
		}
	}
}

class GetThread extends Thread
{
	private DataOutputStream out;
	private String uri;
	
	public GetThread(DataOutputStream out, String uri){
		this.out = out;
		this.uri = uri;
	}
	
	@Override
	public void start(){
		CoapMediator mediator = CoapMediator.GetInstance();
		CoapRequestID id = mediator.Get(uri);
		
		try{ // message format:		REQUEST_ID:id
			out.writeUTF("REQUEST_ID:"+id.getNumericId());
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
	}
}

class PutThread extends Thread
{
	private DataOutputStream out;
	private String uri;
	private String payload;
	private int payloadFormat;
	
	public PutThread(DataOutputStream out, String uri, String payload, int payloadFormat){
		this.out = out;
		this.uri = uri;
		this.payload = payload;
		this.payloadFormat = payloadFormat;
	}
	
	@Override
	public void start(){
		CoapMediator mediator = CoapMediator.GetInstance();
		CoapRequestID id = mediator.Put(uri, payload, payloadFormat);
		
		try{ // message format:		REQUEST_ID:id
			out.writeUTF("REQUEST_ID:"+id.getNumericId());
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
	}
}

class ResponseThread extends Thread
{
	private DataOutputStream out;
	private int requestId;
	private String uri;
	
	public ResponseThread(DataOutputStream out, int requestId, String uri){
		this.out = out;
		this.requestId = requestId;
		this.uri = uri;
	}
	
	@Override
	public void start(){
		CoapMediatorResponse response = null;
		CoapMediator mediator = CoapMediator.GetInstance();
		CoapRequestID id = new CoapRequestID(requestId, uri);
		do {
			response = mediator.GetResponse(id);
		} while(response == null);
		
		try{
			// message format:		RESPONSE:SUCCESS,responseText
			// 									or
			// message format:		RESPONSE:FAILURE,failureMessage
			
			if(response.isResponseValid())
				out.writeUTF("RESPONSE:SUCCESS,"+response.getResponse().getResponseText());
			else
				out.writeUTF("RESPONSE:FAILURE,Response failed!");				
		} catch(Exception e){
			System.out.println("ResponseThread: out socket error.");
		}
	}
}
