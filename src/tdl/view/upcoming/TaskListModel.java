package tdl.view.upcoming;


import java.util.ArrayList;
import java.util.Comparator;

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
		ArrayList<Task> tasksList = treeToArray(baseTask);
		ArrayList<Task> tasksSorted = sort(tasksList);
		Task[] tasksSortedArray = tasksSorted.toArray(new Task[tasksSorted.size()]);
		return tasksSortedArray;
	}
	
	private ArrayList<Task> sort(ArrayList<Task> tasksList) {
		tasksList.sort(new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				return t1.getDeadline().compareTo(t2.getDeadline());
			}
		});
		return tasksList;
	}

	private ArrayList<Task> treeToArray(Task task) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		for(Task child : task.getChildren()) {
			tasks.addAll(treeToArray(child));
		}
		return tasks;
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
