package tdl.view.upcoming;


import javax.swing.AbstractListModel;

import tdl.model.Task;

@SuppressWarnings("serial")
public class TaskListModel extends AbstractListModel<Task> {
	
	private Task[] tasks;

	public TaskListModel(Task baseTask) {
		Task[] tasks = getDateSortedTasks(baseTask);
		this.tasks = tasks;
		
	}

	private Task[] getDateSortedTasks(Task baseTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getElementAt(int index) {
		return tasks[index];
	}

	@Override
	public int getSize() {
		return tasks.length;
	}

	public void setData(Task baseTask) {
		this.tasks = getDateSortedTasks(baseTask);
		fireContentsChanged(this, 0, tasks.length);
	}

	public void refreshView() {
		fireContentsChanged(this, 0, tasks.length);
	}
}
