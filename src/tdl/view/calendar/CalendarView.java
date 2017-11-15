package tdl.view.calendar;

import java.awt.Component;

import javax.swing.JPanel;

import tdl.controller.Controller;

public class CalendarView {
	
	private Controller controller;
	private JPanel jp;

	public CalendarView(Controller controller) {
		jp = new JPanel();
		this.controller = controller; 
	}

	public JPanel getPanel() {
		return jp;
	}

}
