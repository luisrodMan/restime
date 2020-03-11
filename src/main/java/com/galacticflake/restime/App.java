package com.galacticflake.restime;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

public class App {
	
	private JFrame frame = new JFrame("RESTime");
	
	private JToolBar toolbar = new JToolBar();
	private JButton newRequestBtn = new JButton("New Request");
	private JButton saveBtn = new JButton("Save");
	private JTabbedPane tabs = new JTabbedPane();
	private JTree tree = new JTree();
	private JSplitPane contentSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, tabs);
	
	public App() {
		
		toolbar.add(newRequestBtn);
		toolbar.add(saveBtn);
		
		newRequestBtn.addActionListener(event -> newRequestAction());
		
		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(contentSplit);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	private void newRequestAction() {
		NewEndpointDialog.show(endpoint -> {
			Editor editor = new RestEditor(endpoint);
			tabs.addTab(editor.getViewName(), editor.getControlView());
		}, frame);
	}
	
	public static void main(String[] args) {
		new App();
	}
	
}
