package tdl2.view.upcoming;


import javax.swing.AbstractListModel;

import tdl2.model.Task;

@SuppressWarnings("serial")
public class TaskListModel extends AbstractListModel<Task> {
	
	private Task[] tasks;

	public TaskListModel(Task[] tasks) {
		this.tasks = tasks;
		
	}

	@Override
	public Task getElementAt(int index) {
		return tasks[index];
	}

	@Override
	public int getSize() {
		return tasks.length;
	}

	public void setData(Task[] tasks) {
		this.tasks = tasks;
		fireContentsChanged(this, 0, tasks.length);
	}

	public void refreshView() {
		fireContentsChanged(this, 0, tasks.length);
	}
}
