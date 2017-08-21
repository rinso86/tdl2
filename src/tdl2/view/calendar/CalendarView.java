package tdl2.view.calendar;



import javax.swing.JPanel;

import tdl2.controller.Controller;

public class CalendarView {

	private Controller controller;
	private JPanel jp;
	
	public CalendarView(Controller controller) {
		this.controller = controller;
		this.jp = new JPanel();
	}

	public JPanel getPanel() {
		return jp;
	}

}
