package tdl2.view.detail.attachments;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import tdl2.controller.Controller;
import tdl2.controller.treecontroller.TaskNode;
import tdl2.model.Task;

public class AttachPopupListener implements MouseListener {

	private AttachPopup popup;
	private Controller controller;
	private JList<File> list;
	
	public AttachPopupListener(AttachPopup ap, JList<File> attachmentList, Controller controller) {
		this.popup = ap;
		this.list = attachmentList;
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			list.setSelectedIndex(list.locationToIndex(e.getPoint()));
			File selectedFile = (File) list.getSelectedValue();
			popup.show(list, e.getX(), e.getY());
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
