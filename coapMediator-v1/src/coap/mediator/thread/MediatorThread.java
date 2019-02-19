package coap.mediator.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class MediatorThread extends Thread {
	// In Message format: request_type HEADER_SEPARATOR argument1 ARGUMENT_SEPARATOR argument2 ...
	// Change those separators carfully! There are trouble if they are present in the message text. 
	public static final String HEADER_SEPARATOR = "-";
	public static final String ARGUMENT_SEPARATOR = "!";

	protected Socket clientSocket;
	protected DataOutputStream out;
	protected String uri;
	
	public MediatorThread(Socket clientSocket, String uri) throws IOException{
		this.clientSocket = clientSocket;
		this.out = new DataOutputStream(clientSocket.getOutputStream());
		this.uri = uri;
	}
	
	@Override
	public abstract void start();
}
