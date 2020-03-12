package com.galacticflake.restime;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class ProjectExplorer implements View {
	
	private class TreeNode extends DefaultMutableTreeNode {
		
		private String name;
		
		public TreeNode(String name, Object userObject) {
			super(userObject);
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
	};
	
	private JPanel container = new JPanel(new BorderLayout());
	private JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private TreeNode rootNode;
	private JTree tree = new JTree(rootNode = new TreeNode("root", "root"));
	
	public ProjectExplorer() {
		tree.setRootVisible(false);
		container.add(toolbar, BorderLayout.NORTH);
		container.add(new JScrollPane(tree), BorderLayout.CENTER);
	}
	
	public Project getSelectedProject() {
		TreePath path = tree.getSelectionPath();
		if (path != null && path.getPathCount() > 1)
			return (Project) ((TreeNode)path.getPathComponent(1)).getUserObject();
		else {
			if (rootNode.isLeaf())
				return null;
//			if (rootNode.getChildCount() == 1)
			return (Project) ((TreeNode)rootNode.getChildAt(0)).getUserObject();
		}
	}
	
	public void addProject(Project project) {
		TreeNode node = new TreeNode(project.getName(), project);
		rootNode.add(node);
		addEndpoints(project, project.getEndpoints().toArray(new RestEndpoint[0]));
		project.addProjectListener(projectListener);
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		tree.clearSelection();
		tree.setSelectionPath(new TreePath(node.getPath()));
		model.nodeStructureChanged(node);
	}
	
	private TreeNode getNodeFromUserObject(TreeNode root, Object object) {
		for (int i = 0; i < root.getChildCount(); i++) {
			Object userObject = ((TreeNode)root.getChildAt(i)).getUserObject();
			if (userObject == object || (userObject instanceof String && 
					object instanceof String && userObject.toString().equals(object.toString())))
				return (TreeNode) root.getChildAt(i);
		}
		return null;
	}
	
	private void addEndpoints(Project project, RestEndpoint[] endpoints) {
		for (RestEndpoint endpoint : endpoints) {
			String path = endpoint.getFullPath();
			String[] parts = path.replace("://", "$-$").split("/");
			parts[0] = parts[0].replace("$-$", "://");
			System.out.println("parts length: " + parts.length);
			TreeNode parentNode = getNodeFromUserObject(rootNode, project);
			int partIndex = 0;
			for (String part : parts) {
				TreeNode childNode = getNodeFromUserObject(parentNode, part);
				if (childNode == null) {
					childNode = new TreeNode((partIndex==0?"":"/")+part, part);
					parentNode.add(childNode);
				}
				parentNode = childNode;
				partIndex++;
			}
			parentNode.add(new TreeNode(endpoint.getName(), endpoint));
		}
		if (endpoints.length > 0) {
			System.out.println("endpoint added: " + endpoints[0].getEndpoint());
			DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			model.nodeStructureChanged(rootNode);
		}
	}
	
	private ProjectListener projectListener = new ProjectAdapter() {
		
		public void onAddedEndpoints(Project project, RestEndpoint[] endpointsAdded) {
			addEndpoints(project, endpointsAdded);
		}
		
	};

	@Override
	public String getViewName() {
		return "Project Explorer";
	}

	@Override
	public JComponent getControlView() {
		return container;
	}

}
