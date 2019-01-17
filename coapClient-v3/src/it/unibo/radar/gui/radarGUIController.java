package it.unibo.radar.gui;

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

import it.unibo.qactors.akka.QActor;
import it.unibo.radar.coap.RadarPoint;
import it.unibo.radar.coap.client.CoapRadarClient;

public class radarGUIController extends JFrame {

	private static final long serialVersionUID = 1L;
	private static radarGUIController frame;
	
	private TextField txtDistance, txtAngle;
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,250);
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
		
		JPanel pnlButton = new JPanel();
		Button btnGet = new Button("GET");
		Button btnPut = new Button("PUT");
		pnlButton.add(btnGet);
		pnlButton.add(btnPut);
		
		txtArea = new JTextArea(5,40);
		txtArea.setEditable(false);
		txtArea.setAutoscrolls(true);
		
		Container container = getContentPane();
		container.add(pnlDistance);
		container.add(pnlAngle);
		container.add(pnlButton);
		container.add(txtArea);
		
		btnGet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txtArea.setText("GET\n");
				CoapRadarClient client = CoapRadarClient.getInstance();
				RadarPoint point = client.getResourceValue();
				if(point != null)
					txtArea.append("Point Received: "+point.toString());
				else
					txtArea.append("Point not available.");
			}
		});
		
		btnPut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtArea.setText("PUT\n");
				String distance = txtDistance.getText();
				String angle = txtAngle.getText();
				if(distance.isEmpty() || angle.isEmpty())
					txtArea.setText("Insert data into fields to execute a PUT operation.");
				else{
					RadarPoint point = RadarPoint.convertFromString(distance+","+angle);
					if(point != null){
						CoapRadarClient client = CoapRadarClient.getInstance();
						boolean success = client.putResourceValue(point);
						if(success)
							txtArea.append("Resource value changed: "+point.toString());
						else
							txtArea.append("Resource value NOT changed. Error");
					}
					else
						txtArea.append("Invalid data insered (distance: [0,80], angle: [0,360]).");
				}
			}
		});
	}

	public static void setValue(QActor actor, String distance, String angle) {
		setValue(distance, angle);
	}
	
	public static void setValue(String distance, String angle){
		frame.txtArea.setText("distance = " + distance + ", angle = " + angle);
	}
}
