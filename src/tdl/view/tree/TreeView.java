package tdl.view.tree;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.view.tree.popup.TreePopup;
import tdl.view.tree.popup.TreePopupListener;

public class TreeView implements Recipient {
	
	
	private Controller controller;
	private TaskNode baseTaskNode;
	private JLabel treeLabel;
	private JTree jtree;
	private JScrollPane treescrollpane;
	private JPanel jp;
	
	
	public TreeView(Controller controller) {
		
		this.controller = controller;
		this.baseTaskNode = new TaskNode(controller.getBaseTask());
		this.treeLabel = new JLabel("TaskTree");
		
		this.jtree = new JTree(baseTaskNode);
		jtree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jtree.setEditable(true);
		jtree.setCellRenderer(new TaskNodeRenderer());
		jtree.setSelectionPath(new TreePath(baseTaskNode));
		
		TreePopup tp = new TreePopup(this);
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


	public TaskNode getCurrentNode() {
		return (TaskNode) jtree.getSelectionPath().getLastPathComponent();
	}
	
	
	
	
	//-------------------------------------------------------------//
	//-------- Methods for handling messages  ---------------------//
	//-------------------------------------------------------------//
	

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
	
	private void setCurrentTask(Task currentTask) {
		TaskNode tn = getNodeForTask(currentTask);
		TreePath path = new TreePath(tn.getPath());
		jtree.setSelectionPath(path);
		jtree.scrollPathToVisible(path);
	}
	
	private TaskNode getNodeForTask(Task currentTask) {
		TaskNode tn = baseTaskNode.search(currentTask);
		return tn;
	}

	private void addChild(TaskNode parent, TaskNode child) {
		DefaultTreeModel dtm = (DefaultTreeModel) jtree.getModel();
		dtm.insertNodeInto(child, parent, parent.getChildCount());
	}
	
	private void removeNode(TaskNode node) {
		DefaultTreeModel dtm = (DefaultTreeModel) jtree.getModel();
		dtm.removeNodeFromParent(node); 
	}

	
	
	//-------------------------------------------------------------//
	//-------- Methods for handling popup events ------------------//
	//-------------------------------------------------------------//
	
	
	
	public void onPopupAddSubtaskRequested(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onPopupTaskCompleteRequested(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onPopupDeleteTaskRequested(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onPopupReactivateTaskRequested(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
