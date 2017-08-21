package tdl2.controller;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import tdl2.model.Task;



public class TaskTreeModelListener implements TreeModelListener {

	private Controller controller;

	public TaskTreeModelListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void treeNodesChanged(TreeModelEvent tme) {
		//TreePath path = tme.getTreePath();
		//JTreeTaskNode parentnode = (TaskNode) path.getLastPathComponent();
		Object[] children = tme.getChildren();
		if(children != null) {
			for(int i = 0; i < children.length; i++) {
				TaskNode node = (TaskNode) children[i];
				Task task = node.getTask();
				task.setTitle(node.toString());
			}			
		}
	}

	@Override
	public void treeNodesInserted(TreeModelEvent tme) {
		System.out.println(tme.toString());
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent tme) {
		System.out.println(tme.toString());
	}

	@Override
	public void treeStructureChanged(TreeModelEvent tme) {
		System.out.println(tme.toString());
	}

}
