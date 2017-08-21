package tdl2.controller.treecontroller;

import javax.swing.tree.DefaultMutableTreeNode;

import tdl2.model.Task;

@SuppressWarnings("serial")
public class TaskNode extends DefaultMutableTreeNode{

	private Task task;

	public TaskNode(Task task) {
		super(task.getTitle());
		this.task = task;
		for(Task subtask : task.getChildren()) {
			TaskNode jsubtask = new TaskNode(subtask);
			this.add(jsubtask);
		}
	}
	
//	@Override <--- DO NOT override this!!!!!
//	public String toString() {
//		return task.getTitle();
//	}

	public Task getTask() {
		return task;
	}

	public void setTaskDescription(String description) {
		task.setDescription(description);
	}

	public String getTaskDescription() {
		return task.getDescription();
	}
	
	
}
