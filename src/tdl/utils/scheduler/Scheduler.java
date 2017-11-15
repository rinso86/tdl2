package tdl.utils.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;

import tdl.model.Task;
import tdl.utils.statmod.StatMod;

public class Scheduler {
	
	private StatMod statMod;
	private Calendar calendar;

	public Scheduler(StatMod statMod) {
		this.statMod = statMod;
		this.calendar = Calendar.getInstance();
	}


	public ArrayList<ScheduleItem> makeSchedule(Task tree) {
		// TODO: Also make past
		return makeScheduleFuture(tree);
	}


	private ArrayList<ScheduleItem> makeScheduleFuture(Task tree) {
		
		ArrayList<ScheduleItem> schedule = new ArrayList<ScheduleItem>();
		ArrayList<Task> tasks = makeList(tree);
		ArrayList<Task> tasksF = filterByDeadline(tasks);
		ArrayList<Task> tasksFS = sortByDeadline(tasksF);
		
		Date startDate = new Date();
		Date endDate = null;
		for(Task t : tasksFS) {
			
			double estimate = getNetEstimate(t, tasksFS);
			endDate = addSecondsToDate(estimate, startDate);
			ScheduleItem si = new ScheduleItem(startDate, endDate, t);
			schedule.add(si);
			startDate = endDate;
			
//			Date deadline = t.getDeadline();
//			if(endDate.after(deadline)) {
//				throw new Exception("This deadline can not be met!");
//			}
			
		}
		
		return schedule;
	}

	
	private double getNetEstimate(Task task, ArrayList<Task> tasks) {
		double estimate = statMod.estimateTimeToComplete(task);
		ArrayList<Task> tasksC = filterByAncestor(tasks, task);
		for(Task child : tasksC) {
			double estimateChild = getNetEstimate(child, tasksC);
			estimate -= estimateChild;
		}
		return Math.max(estimate, 0);
	}

	private ArrayList<Task> makeList(Task tree) {
		ArrayList<Task> list = new ArrayList<Task>();
		fillListRecursively(tree, list);
		return list;
	}
	
	private void fillListRecursively(Task task, ArrayList<Task> list) {
		list.add(task);
		for(Task child : task.getChildren()) {
			fillListRecursively(child, list);
		}
	}
	
	private ArrayList<Task> filterByAncestor(ArrayList<Task> tasks, Task ancestor) {
		ArrayList<Task> tasksF = new ArrayList<Task>();
		for(Task task : tasks) {
			if( hasAncestor(task, ancestor) ) {
				tasksF.add(task);
			}
		}
		return tasksF;
	}
	
	private ArrayList<Task> filterByPredicate(ArrayList<Task> tasks, Predicate<Task> pred) {
		ArrayList<Task> tasksF = new ArrayList<Task>();
		for(Task t : tasksF) {
			if(pred.test(t)) {
				tasksF.add(t);
			}
		}
		return tasksF;
	}

	private ArrayList<Task> filterByDeadline(ArrayList<Task> tasks) {
		ArrayList<Task> tasksF = new ArrayList<Task>();
		for(Task t : tasks) {
			if(! t.isCompleted() ) {
				if(t.getDeadline() != null) {
					tasksF.add(t);
				}
			}
		}
		return tasksF;
	}
	
	private boolean hasAncestor(Task task, Task ancestor) {
		boolean has = false;
		Task parent = task.getParent();
		if (parent != null) {
			if(parent.getId() == ancestor.getId()) {
				has = true;
			} else {
				has = hasAncestor(parent, ancestor);				
			}
		}
		return has;
	}
	
	private ArrayList<Task> sortByDeadline(ArrayList<Task> tasksF) {
		Collections.sort(tasksF, new Comparator<Task>() {
			public int compare(Task t1, Task t2) {
				Date dl1 = t1.getDeadline();
				Date dl2 = t2.getDeadline();
				return dl1.compareTo(dl2);
			}
		});
		return tasksF;
	}

	private Date addSecondsToDate(double estimate, Date date) {
		// TODO: make sure that seconds WITHIN WORKDAY are counted, not at night. 
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, (int)estimate);
		Date newDate = calendar.getTime();
		return newDate;
	}
	
}
