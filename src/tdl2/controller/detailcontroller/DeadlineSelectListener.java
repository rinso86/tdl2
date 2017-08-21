package tdl2.controller.detailcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tdl2.controller.Controller;

public class DeadlineSelectListener implements ActionListener {

	private Controller controller;

	public DeadlineSelectListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Picker event: " + e.toString());
		//@todo: get the date and put it in the task
	}

}
