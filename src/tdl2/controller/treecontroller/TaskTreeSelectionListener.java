package tdl2.controller.treecontroller;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import tdl2.controller.Controller;


public class TaskTreeSelectionListener implements TreeSelectionListener {

	private Controller controller;

	public TaskTreeSelectionListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath oldPath = e.getOldLeadSelectionPath();
		if(oldPath != null) {
			TaskNode previousNode = (TaskNode) oldPath.getLastPathComponent();
			previousNode.setTaskDescription(controller.getDetailView().getDescription());
		}
		TreePath newPath = e.getPath();
		if(newPath != null) {
			TaskNode currentNode = (TaskNode) newPath.getLastPathComponent();
			controller.getDetailView().setDescription(currentNode.getTaskDescription());
			controller.getDetailView().setDeadline(currentNode.getDeadline());
			controller.getDetailView().setAttachmentList(currentNode.getTask().getAttachments());
		}
	}

}