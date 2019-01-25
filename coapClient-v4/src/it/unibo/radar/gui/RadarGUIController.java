package it.unibo.radar.gui;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import coap.mediator.CoapMediator;
import coap.mediator.CoapMediatorResponse;
import coap.mediator.CoapRequestID;
import it.unibo.radar.coap.RadarPoint;

public class RadarGUIController extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String URI_STRING = "coap://localhost:5683/RadarPoint";
	
	private HashMap<Integer, CoapRequestID> requests;
	private TextField txtDistance, txtAngle, txtResponseId;
	private JTextArea txtArea;
	
	public RadarGUIController(){
		this("Radar GUI Controller");
	}
	
	public RadarGUIController(String title){
		super(title);
		requests = new HashMap<>();
		
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
		
		Container container = getContentPane();
		container.add(pnlDistance);
		container.add(pnlAngle);
		container.add(pnlResponse);
		container.add(pnlButton);
		container.add(txtArea);
		
		btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRequestGET();
			}
		});	
		btnPut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRequestPUT();
			}
		});
		btnResponse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRequestRESPONSE();
			}
		});
	
	}
	
	private void onRequestGET(){
		CoapMediator mediator = CoapMediator.GetInstance();
		CoapRequestID id = mediator.Get(URI_STRING);
		requests.put(id.getNumericId(), id);
		txtArea.append("REQUEST_GET ID: " + id.getNumericId() + "\n");
	}
	
	private void onRequestPUT(){
		String distance = txtDistance.getText();
		String angle = txtAngle.getText();
		if(distance.isEmpty() || angle.isEmpty())
			txtArea.append("Insert data into fields to execute a PUT operation.\n");
		else{
			RadarPoint point = RadarPoint.convertFromString(distance+","+angle);
			if(point != null){
				CoapMediator mediator = CoapMediator.GetInstance();
				CoapRequestID id = mediator.Put(URI_STRING, point.compactToString(), MediaTypeRegistry.TEXT_PLAIN);
				requests.put(id.getNumericId(), id);
				txtArea.append("REQUEST_PUT ID: " + id.getNumericId() + "\n");
			}
			else
				txtArea.append("Invalid data insered (distance: [0,80], angle: [0,360]).\n");
		}
		txtDistance.setText("");
		txtAngle.setText("");
	}

	private void onRequestRESPONSE(){
		String responseID = txtResponseId.getText();
		if(responseID.isEmpty() || responseID.isEmpty())
			txtArea.append("Insert data into the response_id field to execute a RESPONSE operation.\n");
		else{
			int id = Integer.parseInt(responseID);
			CoapMediator mediator = CoapMediator.GetInstance();
			if(requests.containsKey(id)){
				CoapMediatorResponse response = mediator.GetResponse(requests.get(id));
				if(response.isValid()){
					if(response.isAvailable()){
						requests.remove(id);
						txtArea.append("RESPONSE_VALUE: " + response.getResponse().getResponseText() + "\n");
					}
					else
						txtArea.append("RESPONSE_ERROR: Response not available yet.\n");
				}
			}
			else
				txtArea.append("ERROR: RequestID not correct.\n");
		}
		txtResponseId.setText("");
	}

}


