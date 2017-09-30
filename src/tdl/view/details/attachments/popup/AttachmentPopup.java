package tdl.view.details.attachments.popup;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import tdl.view.details.attachments.AttachmentView;


@SuppressWarnings("serial")
public class AttachmentPopup extends JPopupMenu {

	
	@SuppressWarnings("unused")
	private AttachmentView view;
	private JMenuItem openFileItem;
	private JMenuItem deleteFileItem;

	
	public AttachmentPopup(AttachmentView view) {
		this.view = view;
		
		openFileItem = new JMenuItem("Open");
		openFileItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				view.onPopupOpenFileRequested(e);
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
		add(openFileItem);
		
		deleteFileItem = new JMenuItem("Delete");
		deleteFileItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				view.onPopupDeleteFileRequested(e);
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
		add(deleteFileItem);
	}

}
