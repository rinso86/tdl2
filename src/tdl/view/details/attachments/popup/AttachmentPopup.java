package tdl.view.details.attachments.popup;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


@SuppressWarnings("serial")
public class AttachmentPopup extends JPopupMenu {

	private JList<File> list;
	private Controller controller;
	private JMenuItem openFileItem;
	private JMenuItem deleteFileItem;

	public AttachmentPopup(Controller c, JList<File> attachmentList) {
		this.controller = c;
		this.list = attachmentList;
		
		openFileItem = new JMenuItem("Open");
		openFileItem.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				File current = (File) list.getSelectedValue();
				try {
					Desktop.getDesktop().open(current);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
				File current = (File) list.getSelectedValue();
				controller.deleteFileFromCurrentNode(current);
				AttachmentListModel alm = (AttachmentListModel) list.getModel();
				alm.refresh();
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
