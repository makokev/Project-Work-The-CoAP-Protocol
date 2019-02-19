package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import coap.mediator.CoapMediator;
import coap.mediator.request.CoapRequestID;
import coap.mediator.response.CoapMediatorResponse;

public class ResponseMediatorThread extends MediatorThread{

	private int requestId;
	
	public ResponseMediatorThread(Socket clientSocket, int requestId, String uri) throws IOException {
		super(clientSocket, uri);
		this.requestId = requestId;
	}

	@Override
	public void start() {
		// out message format:	"RESPONSE" HEADER_SEPARATOR responseCode ARGUMENT_SEPARATOR responseText/failureMessage
			
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
