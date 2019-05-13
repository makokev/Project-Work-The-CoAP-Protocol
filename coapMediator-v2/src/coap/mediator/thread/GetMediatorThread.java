package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import coap.mediator.CoapMediator;
import coap.mediator.request.CoapRequestID;

public class GetMediatorThread extends MediatorThread{

	public GetMediatorThread(Socket clientSocket, String uri) throws IOException {
		super(clientSocket, uri);
	}

	@Override 
	public void start() {
		// out message format: "REQUEST_ID" HEADER_SEPARATOR id
		System.out.println("GetThread started.");
		
		CoapRequestID id = CoapMediator.Get(uri);
		System.out.println("Generated coapRequestID: " + id);
		
		String message = (new Gson()).toJson(id);
		//String message = "REQUEST_ID"+HEADER_SEPARATOR+id.getNumericId();
		try{
			out.writeUTF(message);
			System.out.println("Message to client --> "+message);
			clientSocket.close();
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
	}
}
