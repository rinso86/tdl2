package tdl.utils.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
		
		// 0th iteration
		Task mostUrgentTask = popMostUrgent(tasks);
		Date startDate = new Date();
		Date endDate = null;
		
		while(mostUrgentTask != null) {
			
			double estimate = statMod.estimateTimeToComplete(mostUrgentTask);
			endDate = addSecondsToDate(estimate, startDate);
			
			ScheduleItem si = new ScheduleItem(startDate, endDate, mostUrgentTask);
			schedule.add(si);
			
			mostUrgentTask = popMostUrgent(tasks);
			startDate = endDate;
		}
		
		return schedule;
	}


	private Task popMostUrgent(ArrayList<Task> tasks) {
		int indx = whereIsMostUrgent(tasks);
		Task mut = tasks.remove(indx);
		return mut;
	}

	private int whereIsMostUrgent(ArrayList<Task> tasks) {
		// TODO Auto-generated method stub
		return 0;
	}

	private ArrayList<Task> makeList(Task tree) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Date addSecondsToDate(double estimate, Date date) {
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, (int)estimate);
		Date newDate = calendar.getTime();
		return newDate;
	}
	
}
