package tdl.view.calendar;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.utils.scheduler.ScheduleItem;

public class CalendarView implements Recipient {
	
	private Controller controller;
	private JEditorPane jep;
	private JPanel jp;

	public CalendarView(Controller controller) {
		this.controller = controller; 
		this.jp = new JPanel();
		
		jep = new JEditorPane();
		jep.setEditable(false);
		jp.add(jep);
		
		updateDisplay();
	}

	@Override
	public void receiveMessage(Message message) {
		System.out.println("UCalendarView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case UPDATED_TASK:
			updateDisplay();
			break;
		case ADDED_SUBTASK:
			updateDisplay();
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
		jep.setText(t);

	}
	
	private ArrayList<ScheduleItem> getSchedule() {
		return controller.getSchedule();
	}

	private String formatSchedule(ArrayList<ScheduleItem> schedule) {
		String formatted = "";
		// TODO
		return formatted;
	}
}
