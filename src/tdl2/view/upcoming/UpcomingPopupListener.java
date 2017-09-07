package tdl2.view.upcoming;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import tdl2.controller.treecontroller.TaskNode;
import tdl2.model.Task;



public class UpcomingPopupListener implements MouseListener {
	
	UpcomingPopup up;

	public UpcomingPopupListener(UpcomingPopup up) {
		this.up = up;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			JList list = (JList) e.getComponent();
			list.setSelectedIndex(list.locationToIndex(e.getPoint()));
			Task selectedTask = (Task) list.getSelectedValue();
			up.setCurrentTask(selectedTask);
			up.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
