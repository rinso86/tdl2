package tdl2.view.upcoming;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tdl2.controller.Controller;
import tdl2.controller.treecontroller.TaskNode;
import tdl2.model.Task;

@SuppressWarnings("serial")
public class UpcomingPopup extends JPopupMenu {
	
	private Controller controller; 
	private Task currentTask;
	private JMenuItem focusOnSubtaskItem;
	
	public UpcomingPopup(Controller c) {
		this.controller = c;
		this.currentTask = null;
		
		focusOnSubtaskItem = new JMenuItem("Focus on subtask");
		focusOnSubtaskItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				controller.setActive(currentTask);
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		add(focusOnSubtaskItem);
		
	}
	
	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task selectedTask) {
		this.currentTask = selectedTask;
	}
	

}
