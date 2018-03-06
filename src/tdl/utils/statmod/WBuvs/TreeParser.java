package tdl.utils.statmod.WBuvs;

import java.util.ArrayList;

import tdl.model.Task;

public class TreeParser {

	public static DistanceMatrix getDistanceMatrix(Task root) {
		
		DistanceMatrix dm = new DistanceMatrix();
		ArrayList<Task> tasks = TreeParser.getTasks(root, new ArrayList<Task>());
		
		for(Task task : tasks) {
			dm.addTask(task);
		}
		
		return dm;
		
	}

	private static ArrayList<Task> getTasks(Task root, ArrayList<Task> tasks) {
		for(Task child : root.getChildren()) {
			ArrayList<Task> subTasks = getTasks(child, new ArrayList<Task>());
			tasks.addAll(subTasks);
		}
		tasks.add(root);
		return tasks;
	}

	public static ArrayList<Task> getTasksCompleted(Task root, ArrayList<Task> tasks) {
		for(Task child : root.getChildren()) {
			ArrayList<Task> subTasks = getTasksCompleted(child, new ArrayList<Task>());
			tasks.addAll(subTasks);
		}
		if(root.isCompleted()) {
			tasks.add(root);			
		}
		return tasks;
	}

	public static Double getGlobalMeanChildcountNet(Task root) {
		// Note that we dont even have to calculate this. In a tree, the mean child count is always 1 - 1/N
	}



}
