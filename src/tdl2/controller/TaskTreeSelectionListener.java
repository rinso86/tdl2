package tdl2.controller;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;


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
		}
	}

}