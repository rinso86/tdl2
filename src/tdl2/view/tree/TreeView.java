package tdl2.view.tree;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import tdl2.model.Task;

public class TreeView {
	
	private Controller controller;
	private JPanel jp;
	private TaskNode taskNode;
	private JLabel treeLabel;
	private JTree jtree;
	private JScrollPane treescrollpane;
	
	public TreeView(Controller controller, TaskNode taskNode) {
		
		this.taskNode = taskNode;
		this.controller = controller;
		this.treeLabel = new JLabel("TaskTree");
		
		this.jtree = new JTree(taskNode);
		jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jtree.setEditable(true);
		jtree.setCellRenderer(new TaskNodeRenderer());
		jtree.setSelectionPath(new TreePath(taskNode));
		
		TreePopup tp = new TreePopup(controller);
		jtree.addMouseListener(new TreePopupListener(tp));
		treescrollpane = new JScrollPane(jtree);
		treescrollpane.setPreferredSize(new Dimension(300, 400));
		
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
        jp.add(treescrollpane, bigFieldConstraints);
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
	
	public Task getCurrentTask() {
		return getCurrentNode().getTask();
	}
	
	public void setCurrentTask(Task currentTask) {
		TaskNode tn = getNodeForTask(currentTask);
		TreePath path = new TreePath(tn.getPath());
		jtree.setSelectionPath(path);
		jtree.scrollPathToVisible(path);
	}




	private TaskNode getNodeForTask(Task currentTask) {
		TaskNode tn = taskNode.search(currentTask);
		return tn;
	}




}
