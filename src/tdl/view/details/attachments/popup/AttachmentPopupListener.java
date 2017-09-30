package tdl.view.details.attachments.popup;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;



public class AttachmentPopupListener implements MouseListener {

	private AttachmentPopup popup;
	
	public AttachmentPopupListener(AttachmentPopup ap) {
		popup = ap;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			Component list = e.getComponent();
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
