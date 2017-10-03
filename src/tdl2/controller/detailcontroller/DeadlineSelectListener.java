package tdl2.controller.detailcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingx.JXDatePicker;

import tdl2.controller.Controller;

public class DeadlineSelectListener implements ActionListener {

	private Controller controller;

	public DeadlineSelectListener(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JXDatePicker picker = (JXDatePicker) e.getSource();
		controller.setDeadlineOnSelectedNode(picker.getDate());
		controller.getUpcomingView().updateList();
	}

}
