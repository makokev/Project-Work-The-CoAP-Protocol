package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import coap.mediator.CoapMediator;
import coap.mediator.request.CoapRequestID;

public class GetMediatorThread extends MediatorThread{

	public GetMediatorThread(Socket clientSocket, String uri) throws IOException {
		super(clientSocket, uri);
	}

	@Override 
	public void start() {
		// out message format: "REQUEST_ID" HEADER_SEPARATOR id
		
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
