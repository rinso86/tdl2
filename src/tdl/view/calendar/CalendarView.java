package tdl.view.calendar;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.utils.scheduler.ScheduleItem;

public class CalendarView implements Recipient {
	
	private Controller controller;
	private JTextPane scheduleTextPane;
	private JButton updateScheduleButton;
	private JTextPane reportTextPane;
	private JButton updateReportButton;
	private JPanel jp;
	private Calendar cal;

	public CalendarView(Controller controller) {
		this.controller = controller; 
		this.cal = new GregorianCalendar();
		this.jp = new JPanel(new GridBagLayout());
		
		scheduleTextPane = new JTextPane();
		scheduleTextPane.setEditable(false);
		updateScheduleButton = new JButton("Update schedule");
		
		reportTextPane = new JTextPane();
		reportTextPane.setEditable(false);
		updateReportButton = new JButton("Update report");
		
		GridBagConstraints leftBigFieldConstraints = new GridBagConstraints();
        leftBigFieldConstraints.gridx = 0;
        leftBigFieldConstraints.gridy = 0;
        leftBigFieldConstraints.gridwidth = 1;
        leftBigFieldConstraints.weightx = leftBigFieldConstraints.weighty = 1.0;
        leftBigFieldConstraints.fill = GridBagConstraints.BOTH;
        
        GridBagConstraints leftSmallFieldConstraints = new GridBagConstraints();
        leftSmallFieldConstraints.gridx = 0;
        leftSmallFieldConstraints.gridy = 1;
        
        GridBagConstraints rightBigFieldConstraints = new GridBagConstraints();
        rightBigFieldConstraints.gridx = 1;
        rightBigFieldConstraints.gridy = 0;
        rightBigFieldConstraints.gridwidth = 1;
        rightBigFieldConstraints.weightx = rightBigFieldConstraints.weighty = 1.0;
        rightBigFieldConstraints.fill = GridBagConstraints.BOTH;
        
        GridBagConstraints rightSmallFieldConstraints = new GridBagConstraints();
        rightSmallFieldConstraints.gridx = 1;
        rightSmallFieldConstraints.gridy = 1;
        
		jp.add(scheduleTextPane, leftBigFieldConstraints);
		jp.add(updateScheduleButton, leftSmallFieldConstraints);
		jp.add(reportTextPane, rightBigFieldConstraints);
		jp.add(updateReportButton, rightSmallFieldConstraints);
		
		updateDisplay();
	}

	@Override
	public void receiveMessage(Message message) {
		System.out.println("CalendarView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case UPDATED_TASK:
		case ADDED_SUBTASK:
			updateDisplay(); //This now happens only on refresh click
			break;
		default:
			break;
		}
	}
	
	public JPanel getPanel() {
		return jp;
	}
	
	private void updateDisplay() {
		ArrayList<ScheduleItem> schedule = getSchedule();
		String t = formatSchedule(schedule);
		scheduleTextPane.setText(t);
		
		cal.add(Calendar.DAY_OF_MONTH, -7);
		Date sevenDaysAgo = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		Date inSevenDays = cal.getTime();
		Task baseTask = controller.getBaseTask();
		String r = "Report:\n" + formatReport(baseTask, sevenDaysAgo, inSevenDays);
		reportTextPane.setText(r);
	}
	
	private String formatReport(Task task, Date from, Date to) {
		String r = "";
		
		if(task.wasActiveDuring(from, to)) {
			String title = task.getTitle();
			double hoursActive = task.getSecondsActive() / (60.0 * 60.0);
			boolean completed = task.isCompleted();
			r += title + "    Active for: " + hoursActive + " hours    Completed: " + completed + "\n"; 			
		}
		
		for(Task child : task.getChildren()) {
			r += "    " + formatReport(child, from, to);
		}
		return r;
	}


	private ArrayList<ScheduleItem> getSchedule() {
		return controller.getSchedule();
	}

	private String formatSchedule(ArrayList<ScheduleItem> schedule) {
		String formatted = "";
		for(ScheduleItem si : schedule) {
			formatted += si.getFrom() + " - ";
			formatted += si.getTo() + " : ";
			formatted += si.getTask().getTitle() + " \n";
		}
		formatted += "";
		return formatted;
	}
}
