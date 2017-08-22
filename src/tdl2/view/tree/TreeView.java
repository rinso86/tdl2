package tdl2.view.tree;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tdl2.controller.Controller;
import tdl2.controller.treecontroller.TaskNode;
import tdl2.controller.treecontroller.TaskTreeModelListener;

public class TreeView {
	
	private Controller controller;
	private JPanel jp;
	private TaskNode taskNode;
	private JTree jtree;
	private JLabel treeLabel;
	
	public TreeView(Controller controller, TaskNode taskNode) {
		
		this.taskNode = taskNode;
		this.controller = controller;
		this.treeLabel = new JLabel("TaskTree");
		
		this.jtree = new JTree(taskNode);
		jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jtree.setEditable(true);
		jtree.setPreferredSize(new Dimension(400, 400));
		jtree.setCellRenderer(new TaskNodeRenderer());
		
		TreePopup tp = new TreePopup(controller);
		jtree.addMouseListener(new TreePopupListener(tp));
		
		
		this.jp = new JPanel(new GridBagLayout());
		GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.NORTHEAST;
        GridBagConstraints bigFieldConstraints = new GridBagConstraints();
        bigFieldConstraints.gridx = 0;
        bigFieldConstraints.gridy = 1;
        bigFieldConstraints.gridwidth = 2;
        GridBagConstraints extraConstraints = new GridBagConstraints();
        extraConstraints.gridx = 1; 
        extraConstraints.gridy = 0;
		
        jp.add(treeLabel, labelConstraints);
        jp.add(jtree, bigFieldConstraints);
	}

	


	public JPanel getPanel() {
		return jp;
	}

	public void addChild(TaskNode parent, TaskNode child) {
		DefaultTreeModel dtm = (DefaultTreeModel) jtree.getModel();
		dtm.insertNodeInto(child, parent, parent.getChildCount());
	}

	public void removeNode(TaskNode node) {
		DefaultTreeModel dtm = (DefaultTreeModel) jtree.getModel();
		dtm.removeNodeFromParent(node); 
	}

	public void setOnStructureChangeListener(TreeModelListener l) {
		jtree.getModel().addTreeModelListener(l);
	}
	
	public void setOnFocusChangeListener(TreeSelectionListener tsl) {
		jtree.addTreeSelectionListener(tsl);
	}

	public TaskNode getCurrentNode() {
		return (TaskNode) jtree.getSelectionPath().getLastPathComponent();
	}




}
