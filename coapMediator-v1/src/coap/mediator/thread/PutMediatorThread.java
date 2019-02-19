package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import coap.mediator.CoapMediator;
import coap.mediator.request.CoapRequestID;

public class PutMediatorThread extends MediatorThread{

	private String payload;
	private int payloadFormat;
	
	public PutMediatorThread(Socket clientSocket, String uri,String payload, int payloadFormat) throws IOException {
		super(clientSocket, uri);
		this.payload = payload;
		this.payloadFormat = payloadFormat;
	}

	@Override
	public void start() {
		// out message format: "REQUEST_ID" HEADER_SEPARATOR id
		
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
