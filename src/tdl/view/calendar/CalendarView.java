package tdl.view.calendar;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;


import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.utils.scheduler.ScheduleItem;

public class CalendarView implements Recipient {
	
	private Controller controller;
	private JTextPane jep;
	private JPanel jp;

	public CalendarView(Controller controller) {
		this.controller = controller; 
		this.jp = new JPanel(new GridBagLayout());
		
		jep = new JTextPane();
		jep.setEditable(false);
		
		GridBagConstraints bigFieldConstraints = new GridBagConstraints();
        bigFieldConstraints.gridx = 0;
        bigFieldConstraints.gridy = 0;
        bigFieldConstraints.gridwidth = 1;
        bigFieldConstraints.weightx = bigFieldConstraints.weighty = 1.0;
        bigFieldConstraints.fill = GridBagConstraints.BOTH;
        
		jp.add(jep, bigFieldConstraints);
		
		updateDisplay();
	}

	@Override
	public void receiveMessage(Message message) {
		System.out.println("CalendarView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case UPDATED_TASK:
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
		for(ScheduleItem si : schedule) {
			formatted += si.getFrom() + " - ";
			formatted += si.getTo() + " : ";
			formatted += si.getTask().getTitle() + " \n";
		}
		formatted += "";
		return formatted;
	}
}
