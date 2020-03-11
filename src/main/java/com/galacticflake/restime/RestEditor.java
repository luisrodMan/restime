package com.galacticflake.restime;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RestEditor extends JPanel implements Editor {

	private static final long serialVersionUID = 1L;
	
	private JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JComboBox<String> requestMethodcb = new JComboBox<>();
	private JComboBox<String> requestContentTypecb = new JComboBox<>();
	private JTextField endpointtxt = new JTextField(30);
	private JButton runBtn = new JButton("Run");
	
	private JTextArea requestHeaderArea = new JTextArea();
	private JTextArea requestContentArea = new JTextArea();
	private JTextArea responseHeaderArea = new JTextArea();
	private JTextArea responseContentArea = new JTextArea();
	
	private class Container extends JPanel {

		private static final long serialVersionUID = 1L;
		
		public Container(String title, JComponent component) {
			this(title, component, null);
		}
		public Container(String title, JComponent component, JComponent toolbar) {
			setLayout(new BorderLayout());
			add(new JLabel(title), BorderLayout.NORTH);
			if (toolbar != null) {
				JPanel container = new JPanel(new BorderLayout());
				container.setBorder(BorderFactory.createEmptyBorder());
				container.add(toolbar, BorderLayout.NORTH);
				container.add(component);
				component = container;
			}
			add(component, BorderLayout.CENTER);
		}
		
	}
	
	private JComponent createToolbar() {
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolbar.add(new JLabel("Content-Type"));
		toolbar.add(requestContentTypecb);
		return toolbar;
	}
	
	private class RequestContentView extends Container {

		private static final long serialVersionUID = 1L;

		public RequestContentView() {
			super("Request Content", new JScrollPane(requestContentArea), createToolbar());
		}
		
	}
	
	private JSplitPane requestSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new Container("Request Header", new JScrollPane(requestHeaderArea)), new RequestContentView());
	private JSplitPane responseSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new Container("Response Header", new JScrollPane(responseHeaderArea)), new Container("Response Content", new JScrollPane(responseContentArea)));
	private JSplitPane editorSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, requestSplit, responseSplit);
	
	private RestEndpoint endpoint;
	
	public RestEditor(RestEndpoint endpoint) {
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(editorSplit, BorderLayout.CENTER);
		
		toolbar.add(new JLabel("Method"));
		toolbar.add(requestMethodcb);
		toolbar.add(endpointtxt);
		toolbar.add(runBtn);
		
		runBtn.addActionListener(event -> runAction());
		
		requestMethodcb.addItem("GET");
		requestMethodcb.addItem("POST");
		requestMethodcb.addItem("PUT");
		requestMethodcb.addItem("DELETE");
		
		requestContentTypecb.setEditable(true);
		requestContentTypecb.addItem("application/json");
		requestContentTypecb.addItem("x-www-form-urlencoded");
		requestContentTypecb.addItem("text/plain");
		requestContentTypecb.addItem("multipart/form-data");
		
		this.endpoint = endpoint;
		if (endpoint.getEndpoint() != null)
			endpointtxt.setText(endpoint.getEndpoint());
		
		requestMethodcb.setSelectedItem(endpoint.getMethod());
	}
	
	private void runAction() {
		responseHeaderArea.setText("");
		responseContentArea.setText("");
		
		String method = requestMethodcb.getSelectedItem().toString();
		String endpoint = endpointtxt.getText();
		String requestContent = requestContentArea.getText();
		
		String requestContentType = requestContentTypecb.getSelectedItem().toString();
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
			connection.setDoOutput(true);
			
			// request headers
			String requestHeaderContent = requestHeaderArea.getText();
			if (requestHeaderContent != null) {
				requestHeaderContent = requestHeaderContent.trim();
				if (!requestHeaderContent.isEmpty()) {
					String[] headerLine = requestHeaderContent.split(System.lineSeparator());
					for (String line : headerLine) {
						int i = line.indexOf(':');
						if (i > -1) {
							connection.setRequestProperty(line.substring(0, i).trim(), line.substring(i+1).trim());
						}
					}
				}
			}
			
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", requestContentType);
			if (method.equals("GET") || requestContent == null) {
				connection.connect();
			} else {
				byte[] content = requestContentArea.getText().getBytes();
				connection.setRequestProperty("Content-Length", content.length+"");
				connection.getOutputStream().write(content);
				connection.getOutputStream().flush();
			}
			
			// response header
			Map<String, List<String>> responseHeaders = connection.getHeaderFields();
			String responseHeaderContent = "";
			for (String headerName : responseHeaders.keySet())
				responseHeaderContent += headerName + ": " + responseHeaders.get(headerName) + System.lineSeparator();
			responseHeaderArea.setText(responseHeaderContent);
			
			// response content
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String responseLine = null;
			while ((responseLine=reader.readLine()) != null) {
				responseContentArea.setText(responseContentArea.getText() + System.lineSeparator() + responseLine);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getViewName() {
		return endpoint.getName();
	}

	public JComponent getControlView() {
		return this;
	}

	public boolean isDirty() {
		return false;
	}

	public void save() {
		
	}

	public void saveAs() {
		
	}

}
