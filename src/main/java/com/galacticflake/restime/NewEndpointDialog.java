package com.galacticflake.restime;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewEndpointDialog extends JFrame {
	
	public static void show(Consumer<RestEndpoint> consumer) {
		show(consumer, null);
	}
	
	public static void show(Consumer<RestEndpoint> consumer, Component component) {
		JFrame frame = new NewEndpointDialog(consumer);
		frame.setLocationRelativeTo(component);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static final long serialVersionUID = 1L;
	private JTextField nameTxt = new JTextField(40);
	private JTextField endpointTxt = new JTextField(40);
	
	public NewEndpointDialog(Consumer<RestEndpoint> consumer) {
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(new JLabel("Name"));
		p1.add(nameTxt);
		p2.add(new JLabel("Endpoint"));
		p2.add(endpointTxt);
		
		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		
		JButton aceptBtn = new JButton("Acept");
		JButton cancelBtn = new JButton("Cancel");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		cancelBtn.addActionListener(event -> dispose());
		aceptBtn.addActionListener(event -> {
			String name = nameTxt.getText();
			String endpoint = endpointTxt.getText();
			RestEndpoint rendpoint = new RestEndpoint(name, endpoint);
			consumer.accept(rendpoint);
			dispose();
		});
		
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p3.add(aceptBtn);
		p3.add(cancelBtn);
		add(p3, BorderLayout.SOUTH);
	}

}
