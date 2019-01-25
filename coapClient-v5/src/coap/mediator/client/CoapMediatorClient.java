package coap.mediator.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import coap.mediator.CoapMediatorResponseCode;
import coap.mediator.CoapRequestID;
import coap.mediator.MediatorMessage;
import it.unibo.radar.RadarPoint;

public class CoapMediatorClient {
	
	private static final String REMOTE_MEDIATOR_HOST = "localhost";
	private static final int REMOTE_MEDIATOR_PORT = 5633;
	private static final String HEADER_SEPARATOR = "-";
	private static final String ARGUMENT_SEPARATOR = "!";
	
	public static CoapRequestID Get(String resourceURI){
		// out format:	GET|uri
		// in  format:	REQUEST_ID|id
		try {
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			String message = "GET"+HEADER_SEPARATOR+resourceURI;
			System.out.println("Message out: '"+message+"'\n");
			outToMediator.writeUTF(message);
			message = inFromMediator.readUTF();
			clientSocket.close();
			System.out.println("Message in: '"+message+"'\n");
			String type = message.split(HEADER_SEPARATOR)[0];
			if(type.equals("REQUEST_ID"))
			{
				int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
				CoapRequestID requestId = new CoapRequestID(id, resourceURI);
				System.out.println("REQUEST_GET ID: " + requestId.getNumericId()+"\n");
				return requestId;
			}else
				System.out.println("REQUEST_GET: Response error.\n");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Socket error.\n");
		}
		return null;
	}
	
	public static CoapRequestID Put(String resourceURI, RadarPoint point){
		// out format:	PUT|uri,payload,payloadFormat
		// in  format:	REQUEST_ID|id
				
		try {
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			String message = "PUT"+HEADER_SEPARATOR+resourceURI+ARGUMENT_SEPARATOR+point.compactToString()+ARGUMENT_SEPARATOR+MediaTypeRegistry.TEXT_PLAIN;
			System.out.println("Message out: '"+message+"'\n");
			outToMediator.writeUTF(message);
			message = inFromMediator.readUTF();
			clientSocket.close();
			System.out.println("Message in: '"+message+"'\n");
			String type = message.split(HEADER_SEPARATOR)[0];
			if(type.equals("REQUEST_ID"))
			{
				int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
				CoapRequestID requestId = new CoapRequestID(id, resourceURI);
				System.out.println("REQUEST_PUT ID: " + requestId.getNumericId() + "\n");
				return requestId;
			}else
				System.out.println("REQUEST_PUT: Response error.\n");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Socket error.\n");
		}
		return null;
	}
	
	public static MediatorMessage GetResponse(CoapRequestID requestId){
		try{
			Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
			clientSocket.setSoTimeout(60000); // 60 seconds
			DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
			
			String message = "RESPONSE"+HEADER_SEPARATOR+requestId.getUri().toString()+ARGUMENT_SEPARATOR+requestId.getNumericId();
			System.out.println("Message out: '"+message+"'\n");
			outToMediator.writeUTF(message);
			message = inFromMediator.readUTF();
			clientSocket.close();
			System.out.println("Message in: '"+message+"'\n");
			String type = message.split(HEADER_SEPARATOR)[0];
			if(type.equals("RESPONSE"))
			{
				String result = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[0];
				CoapMediatorResponseCode code = CoapMediatorResponseCode.valueOf(result);
				String responseText = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[1];
				String response = (code.isSuccess()) ? "RESPONSE_VALUE: " : "RESPONSE_ERROR: ";
				response += responseText;
				return new MediatorMessage(code, response);
			}else{
				System.out.println("REQUEST_RESPONSE: Response error.\n");
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Socket error.\n");
		}
		CoapMediatorResponseCode code = CoapMediatorResponseCode.RESPONSE_FORMAT_ERROR;
		return new MediatorMessage(code, "RESPONSE_ERROR: "+code.getDescription());
	}

}