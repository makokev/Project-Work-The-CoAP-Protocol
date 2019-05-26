package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import coap.mediator.CoapMediator;
import coap.mediator.request.ClientMediatorRequestID;

public class MediatorThreadPut extends MediatorThread{

	private String payload;
	private int payloadFormat;
	
	public MediatorThreadPut(Socket clientSocket, String uri,String payload, int payloadFormat) throws IOException {
		super(clientSocket, uri);
		this.payload = payload;
		this.payloadFormat = payloadFormat;
	}

	@Override
	public void start() {
		// out message format: "REQUEST_ID" HEADER_SEPARATOR id

		ClientMediatorRequestID id = CoapMediator.Put(uri, payload, payloadFormat);
		System.out.println("PutThread started.");
		String message = (new Gson()).toJson(id);
		//String message = "REQUEST_ID"+HEADER_SEPARATOR+id.getNumericId();
		try{
			out.writeUTF(message);
			System.out.println("Message --> "+message);
			clientSocket.close();
		} catch(Exception e){
			System.out.println("GetThread: out socket error.");
		}
	}
}
