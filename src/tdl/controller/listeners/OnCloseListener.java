package tdl.controller.listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import tdl.controller.Controller;

public class OnCloseListener extends WindowAdapter {
	
	private Controller controller;

	public OnCloseListener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		controller.saveModelToFile();
		super.windowClosing(e);
	}
}
