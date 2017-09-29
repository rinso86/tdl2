package tdl.view.details.attachments;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tdl.utils.FileDrop;
import tdl.utils.FileDrop.Listener;
import tdl.view.details.DetailView;

public class AttachmentView {

	private DetailView view;
	private JPanel jp;
	private AttachmentPopup ap;
	private FileDrop fileDrop;
	private JList<File> attachmentList;
	private JScrollPane attchscrollpane;

	public AttachmentView(DetailView view) {
		view = view;
		jp = new JPanel();
		
		attachmentList = new JList<File>(new AttachmentListModel(new ArrayList<File>()));
		attachmentList.setCellRenderer(new AttachmentListCellRenderer(view));
		attchscrollpane = new JScrollPane(attachmentList);
		attchscrollpane.setPreferredSize(new Dimension(300, 400));
		
		ap = new AttachmentPopup(c, attachmentList);
		attachmentList.addMouseListener(new AttachmentPopupListener(ap, attachmentList, view));
		
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
