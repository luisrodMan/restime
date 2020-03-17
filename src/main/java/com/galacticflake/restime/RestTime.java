package com.galacticflake.restime;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;

import com.galacticflake.restime.projectsupport.Project;
import com.galacticflake.restime.projectsupport.ProjectExplorer;
import com.galacticflake.restime.projectsupport.ProjectExporter;
import com.galacticflake.restime.projectsupport.ProjectLoader;

public class RestTime {
	
	public static final String PROJECT_EXT = "rtime";
	public static final String PROJECT_DESC = "ResTime file";
	
	private JFrame frame = new JFrame("RESTime");
	
	private JToolBar toolbar = new JToolBar();
	private JButton openBtn = new JButton("Open");
	private JButton saveBtn = new JButton("Save");
	private JButton saveAsBtn = new JButton("SaveAs");
	private JButton newRequestBtn = new JButton("New Request");
	
	private JTabbedPane tabs = new JTabbedPane();
	private Map<JComponent, IView> tabsViewsMap = new HashMap<>();
	private IView selectedTabView = null;
	
	private ProjectExplorer projectExplorer = new ProjectExplorer();
	private JSplitPane contentSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectExplorer.getControlView(), tabs);
	
	public RestTime() {
	
		Project project =new Project("luisprj");
		try {
			ProjectExporter.export(project, new File("/home/luis/Desktop/luisborrar.xd"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				selectedTabView = tabs.getSelectedIndex()<1? null : tabsViewsMap.get(tabs.getSelectedComponent());
				if (selectedTabView == null || !(selectedTabView instanceof IEditor))
					saveBtn.setEnabled(false);
				else
					saveBtn.setEnabled(((IEditor)selectedTabView).isDirty());
			}
		});
		
		openBtn.addActionListener(event -> openProjectAction());
		saveBtn.addActionListener(event -> ((IEditor)selectedTabView).save());
		saveAsBtn.addActionListener(event -> ((IEditor)selectedTabView).saveAs());
		newRequestBtn.addActionListener(event -> newRequestAction());
		
		toolbar.add(saveBtn);
		toolbar.add(saveAsBtn);
		toolbar.add(openBtn);
		toolbar.add(newRequestBtn);
		
		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(contentSplit);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	private void openProjectAction() {
		try {
			JFileChooser fchooser = new JFileChooser();
			fchooser.setFileFilter(new FileNameExtensionFilter(PROJECT_DESC, PROJECT_EXT));
			if (fchooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				Project project = ProjectLoader.load(fchooser.getSelectedFile());
				projectExplorer.addProject(project);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void newRequestAction() {
		NewEndpointDialog.show(endpoint -> {
			Project project = projectExplorer.getSelectedProject();
			if (project == null) {
				project = new Project("No saved");
				projectExplorer.addProject(project);
			}
			project.addEndpoint(endpoint);
			openEndpointAction(endpoint);
		}, frame);
	}
	
	private void openEndpointAction(RestEndpoint endpoint) {
		IEditor editor = new RestEditor(endpoint);
		tabs.addTab(editor.getViewName(), editor.getControlView());
	}
	
	public static void main(String[] args) {
		new RestTime();
	}
	
}
