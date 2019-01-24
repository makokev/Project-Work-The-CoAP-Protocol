package it.unibo.radar.gui;

import javax.swing.JScrollPane;
import java.util.HashMap;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.io.*;
import java.net.*;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import coap.mediator.CoapRequestID;
import it.unibo.qactors.akka.QActor;
import it.unibo.radar.coap.RadarPoint;

public class radarGUIController extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	private static final String REMOTE_MEDIATOR_HOST = "localhost";
	private static final int REMOTE_MEDIATOR_PORT = 5633;
	private static final String HEADER_SEPARATOR = "-";
	private static final String ARGUMENT_SEPARATOR = "_";
	private static radarGUIController frame;
	
	private HashMap<Integer, CoapRequestID> map;
	private TextField txtDistance, txtAngle, txtResponseId;
	private JTextArea txtArea;
	
	public static void startGUI(QActor actor){
		startGUI();
	}
	
	public static void startGUI(){
		frame = new radarGUIController();
		frame.setVisible(true);
	}
	
	private radarGUIController(){
		super("Radar GUI Controller");
		map = new HashMap<>();		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,400);
		this.setResizable(false);
		setLayout(new FlowLayout());
			
		JPanel pnlDistance = new JPanel();
		pnlDistance.setAlignmentX(Component.LEFT_ALIGNMENT);
		Label lable1 = new Label("Distance");
		txtDistance = new TextField(50);
		pnlDistance.add(lable1);
		pnlDistance.add(txtDistance);
		
		JPanel pnlAngle = new JPanel();
		Label lable2 = new Label("Angle");
		txtAngle = new TextField(50);
		pnlAngle.add(lable2);
		pnlAngle.add(txtAngle);
		
		JPanel pnlResponse = new JPanel();
		Label lable3 = new Label("Response id");
		txtResponseId = new TextField(50);
		pnlResponse.add(lable3);
		pnlResponse.add(txtResponseId);
		
		
		JPanel pnlButton = new JPanel();
		Button btnGet = new Button("GET");
		Button btnPut = new Button("PUT");
		Button btnResponse = new Button("RESPONSE");
		pnlButton.add(btnGet);
		pnlButton.add(btnPut);
		pnlButton.add(btnResponse);
		
		txtArea = new JTextArea(12,40);
		txtArea.setEditable(false);
		txtArea.setAutoscrolls(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.add(txtArea);
		scrollPane.setViewportView(txtArea);
		
		Container container = getContentPane();
		container.add(pnlDistance);
		container.add(pnlAngle);
		container.add(pnlResponse);
		container.add(pnlButton);
		container.add(scrollPane);
		
		btnGet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// out format:			GET|uri
				// in  format:			REQUEST_ID|id
				txtArea.append("- GET\n");
				try {
					@SuppressWarnings("resource")
					Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
					clientSocket.setSoTimeout(60000); // 60 seconds
					DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
					DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
					
					String message = "GET"+HEADER_SEPARATOR+URI_STRING;
					txtArea.append("Message --> '"+message+"'\n");
					outToMediator.writeUTF(message);
					message = inFromMediator.readUTF();
					txtArea.append("Message <-- '"+message+"'\n");
					String type = message.split(HEADER_SEPARATOR)[0];
					CoapRequestID requestId = null;
					if(type.equals("REQUEST_ID"))
					{
						int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
						requestId = new CoapRequestID(id, URI_STRING);
						txtArea.append("REQUEST_GET ID: " + requestId.getNumericId()+"\n");
						clientSocket.close();
					}else {
						txtArea.append("REQUEST_GET: Response error.\n");
						System.exit(-1);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					txtArea.append("Socket error.\n");
				}
			}
		});
		
		btnPut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// out format:			PUT|uri,payload,payloadFormat
				// in  format:			REQUEST_ID|id
				
				txtArea.append("- PUT\n");
				String distance = txtDistance.getText();
				String angle = txtAngle.getText();
				if(distance.isEmpty() || angle.isEmpty())
					txtArea.append("Insert data into fields to execute a PUT operation.\n");
				else{
					RadarPoint point = RadarPoint.convertFromString(distance+","+angle);
					if(point != null){
						try {
							@SuppressWarnings("resource")
							Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
							clientSocket.setSoTimeout(60000); // 60 seconds
							DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
							DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
							
							String message = "PUT"+HEADER_SEPARATOR+URI_STRING+ARGUMENT_SEPARATOR+point.compactToString()+ARGUMENT_SEPARATOR+MediaTypeRegistry.TEXT_PLAIN;
							txtArea.append("Message --> '"+message+"'\n");
							outToMediator.writeUTF(message);
							message = inFromMediator.readUTF();
							txtArea.append("Message <-- '"+message+"'\n");
							String type = message.split(HEADER_SEPARATOR)[0];
							if(type.equals("REQUEST_ID"))
							{
								int id = Integer.parseInt(message.split(HEADER_SEPARATOR)[1]);
								CoapRequestID requestId = new CoapRequestID(id, URI_STRING);
								map.put(requestId.getNumericId(), requestId);
								txtArea.append("REQUEST_PUT ID: " + requestId.getNumericId() + "\n");
							}else
								txtArea.append("REQUEST_PUT: Response error.\n");	
							clientSocket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
							txtArea.append("Socket error.\n");
						}
					}
					else
						txtArea.append("Invalid data insered (distance: [0,80], angle: [0,360]).\n");
				}
				txtDistance.setText("");
				txtAngle.setText("");
			}
		});
		
		btnResponse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// out format:			RESPONSE|uri,requestId
				// in  format:			RESPONSE|SUCCESS,responseText
				// 							or
				// in  format:			RESPONSE|FAILURE,failureMessage
				txtArea.append("- RESPONSE\n");
				String responseID = txtResponseId.getText();
				if(responseID.isEmpty() || responseID.isEmpty())
					txtArea.append("Insert data into the response_id field to execute a RESPONSE operation.\n");
				else{
					try {
						@SuppressWarnings("resource")
						Socket clientSocket = new Socket(REMOTE_MEDIATOR_HOST, REMOTE_MEDIATOR_PORT);
						clientSocket.setSoTimeout(60000); // 60 seconds
						DataOutputStream outToMediator = new DataOutputStream(clientSocket.getOutputStream());
						DataInputStream inFromMediator = new DataInputStream(clientSocket.getInputStream());
						
						String message = "RESPONSE"+HEADER_SEPARATOR+URI_STRING+ARGUMENT_SEPARATOR+responseID;
						txtArea.append("Message --> '"+message+"'\n");
						outToMediator.writeUTF(message);
						message = inFromMediator.readUTF();
						txtArea.append("Message <-- '"+message+"'\n");
						String type = message.split(HEADER_SEPARATOR)[0];
						if(type.equals("RESPONSE"))
						{
							String result = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[0];
							String responseText = message.split(HEADER_SEPARATOR)[1].split(ARGUMENT_SEPARATOR)[1];
							if(result.equals("SUCCESS"))
								txtArea.append("RESPONSE_VALUE: " + responseText + "\n");
							else
								txtArea.append("RESPONSE_ERROR: " + responseText + "\n");
						}else
							txtArea.append("REQUEST_RESPONSE: Response error.\n");
						clientSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						txtArea.append("Socket error.\n");
					}
				}
				txtResponseId.setText("");
			}
		});
	}

	public static void setValue(QActor actor, String distance, String angle) {
		setValue(distance, angle);
	}
	
	public static void setValue(String distance, String angle){
		frame.txtArea.append("distance = " + distance + ", angle = " + angle + "\n");
	}
}
