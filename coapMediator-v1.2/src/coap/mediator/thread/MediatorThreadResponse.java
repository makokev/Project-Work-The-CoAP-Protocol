package coap.mediator.thread;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;

import coap.mediator.CoapMediator;
import coap.mediator.request.ClientMediatorRequestID;
import coap.mediator.response.CoapMediatorResponse;

public class MediatorThreadResponse extends MediatorThread{

	private int requestId;
	private Code requestType;
	
	public MediatorThreadResponse(Socket clientSocket, Code requestType, int requestId, String uri) throws IOException {
		super(clientSocket, uri);
		this.requestType = requestType;
		this.requestId = requestId;
	}

	@Override
	public void start() {
		// out message format:	"RESPONSE" HEADER_SEPARATOR responseCode ARGUMENT_SEPARATOR responseText/failureMessage
			
		CoapMediatorResponse response = null;
		ClientMediatorRequestID id = new ClientMediatorRequestID(requestType, requestId, uri);
		System.out.println("ResponseThread started.");
		try{
			response = CoapMediator.GetResponse(id);	
			String message = (new Gson()).toJson(response);
			out.writeUTF(message);
			System.out.println("Message to client --> "+message);
			System.out.println("RESP CODE: " + response.getRequestId().getRequestType());
			System.out.println("ResponseThread stopped.");
			clientSocket.close();
		} catch(Exception e){
			System.out.println("ResponseThread: out socket error.");
			e.printStackTrace();
		}
	}
}
