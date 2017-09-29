package tdl.view.details.attachments.popup;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.SwingUtilities;



public class AttachmentPopupListener implements MouseListener {

	private AttachmentPopup popup;
	private JList<File> list;
	
	public AttachmentPopupListener(AttachmentPopup ap, JList<File> attachmentList) {
		this.popup = ap;
		this.list = attachmentList;
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
