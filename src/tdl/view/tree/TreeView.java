package tdl.view.tree;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.UUID;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.MessageType;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.view.tree.popup.TreePopup;
import tdl.view.tree.popup.TreePopupListener;
import tdl2.controller.treecontroller.TaskNode;

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
		System.out.println("TreeView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case NEW_TASK_ACTIVE:
			setCurrentTask( (Task) message.getHeaders().get("task"));
			break;
		case ADDED_SUBTASK:
			Task task = (Task) message.getHeaders().get("task");
			Task parent = task.getParent();
			TaskNode parentNode = getNodeForTask(parent);
			TaskNode childNode = new TaskNode( task );
			addChild(parentNode, childNode);
			refresh();
			break;
		case UPDATED_TASK:
		case COMPLETED_TASK:
		case REACTIVATED_TASK:
			refresh();
			break;
		case DELETED_TASK:
			pruneTree(baseTaskNode);
			refresh();
		case DELETED_FILE:
			break;
		default:
			break;
		}
	}

	/*
	 * deletes all branches that have lost their reference to a task
	 */
	private void pruneTree(TaskNode base) {
		if(base.getTask() == null) {
			removeNode(base);
		}else {
			int I = base.getChildCount();
			TaskNode[] children = new TaskNode[I];
			for(int i = 0; i < I; i++) {
				children[i] = (TaskNode) base.getChildAt(i);
			}
			for(TaskNode child : children) {
				pruneTree(child);
			}
		}
	}

	private void refresh() {
//		Task baseTask = controller.getBaseTask();
//		Task currentTask = controller.getCurrentTask();
//		TaskNode baseTaskNode = new TaskNode(baseTask);
//		TaskNode currentTaskNode = baseTaskNode.search(currentTask);
//		
		DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
		model.reload();
//		jtree.setSelectionPath(new TreePath(currentTaskNode));
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
	
	private TaskNode getNodeForPath(TreePath path) {
		TaskNode tn = (TaskNode) path.getLastPathComponent();
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
		TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
		TaskNode tn = getNodeForPath(path);
		Message m = new Message(MessageType.ADD_SUBTASK_REQUEST);
		m.addHeader("task", tn.getTask());
		System.out.println("TreeView now messaging " + m.getMessageType() + " for task "+ tn.getTask().getTitle());
		controller.receiveMessage(m);
	}

	public void onPopupTaskCompleteRequested(MouseEvent e) {
		TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
		TaskNode tn = getNodeForPath(path);
		Message m = new Message(MessageType.COMPLETE_TASK_REQUEST);
		m.addHeader("task", tn.getTask());
		System.out.println("TreeView now messaging " + m.getMessageType() + " for task "+ tn.getTask().getTitle());
		controller.receiveMessage(m);
	}

	public void onPopupDeleteTaskRequested(MouseEvent e) {
		TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
		TaskNode tn = getNodeForPath(path);
		Message m = new Message(MessageType.DELETE_TASK_REQUEST);
		m.addHeader("task", tn.getTask());
		System.out.println("TreeView now messaging " + m.getMessageType() + " for task "+ tn.getTask().getTitle());
		controller.receiveMessage(m);
	}

	public void onPopupReactivateTaskRequested(MouseEvent e) {
		TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
		TaskNode tn = getNodeForPath(path);
		Message m = new Message(MessageType.REACTIVATE_TASK_REQUEST);
		m.addHeader("task", tn.getTask());
		System.out.println("TreeView now messaging " + m.getMessageType() + " for task "+ tn.getTask().getTitle());
		controller.receiveMessage(m);
	}


}
