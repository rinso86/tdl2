package tdl2.view.detail.attachments;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tdl2.controller.Controller;
import tdl2.utils.FileDrop;
import tdl2.utils.FileDrop.Listener;

public class AttachmentView {

	private Controller controller;
	private JPanel jp;
	private AttachPopup ap;
	private FileDrop fileDrop;
	private JList<File> attachmentList;
	private JScrollPane attchscrollpane;

	public AttachmentView(Controller c) {
		controller = c;
		jp = new JPanel();
		
		attachmentList = new JList<File>(new AttachmentListModel(new ArrayList<File>()));
		attchscrollpane = new JScrollPane(attachmentList);
		attchscrollpane.setPreferredSize(new Dimension(300, 400));
		
		ap = new AttachPopup(c, attachmentList);
		attachmentList.addMouseListener(new AttachPopupListener(ap, attachmentList, controller));
		
		jp.add(attchscrollpane);
	}

	public JPanel getJPanel() {
		return jp;
	}
	
	public void setOnAttachmChangeListener(Listener listener) {
		fileDrop = new FileDrop (attachmentList, listener);
	}
	
	public void setAttachmentList(ArrayList<File> attachments) {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.setData(attachments);
	}
	
	public void refresh() {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.refresh();
	}

}
