package tdl.view.details.attachments;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.utils.FileDrop;
import tdl.utils.FileDrop.Listener;
import tdl.view.details.DetailView;
import tdl.view.details.attachments.popup.AttachmentPopup;
import tdl.view.details.attachments.popup.AttachmentPopupListener;

public class AttachmentView implements Recipient {

	private DetailView view;
	private JPanel jp;
	private AttachmentPopup ap;
	private FileDrop fileDrop;
	private JList<File> attachmentList;
	private JScrollPane attchscrollpane;

	public AttachmentView(DetailView view) {
		view = view;
		jp = new JPanel();
		
		attachmentList = new JList<File>(new AttachmentListModel(getCurrentAttachmentList()));
		attachmentList.setCellRenderer(new AttachmentListCellRenderer(this));
		attchscrollpane = new JScrollPane(attachmentList);
		attchscrollpane.setPreferredSize(new Dimension(300, 400));
		
		ap = new AttachmentPopup(this);
		attachmentList.addMouseListener(new AttachmentPopupListener(ap));
		
		fileDrop = new FileDrop (attachmentList, new MyFileDropListener());

		jp.add(attchscrollpane);
	}
	
	private class MyFileDropListener implements Listener {
		@Override
		public void filesDropped(File[] files) {
			ArrayList<File> savedFiles = new ArrayList<File>();
			for(int i = 0; i < files.length; i++) {
				File savedFile = null;
				try {
					savedFile = controller.getResourceManager().saveToResources(files[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				savedFiles.add(savedFile);
			}
			controller.getTreeView().getCurrentNode().getTask().addAttachments(savedFiles);
			controller.getDetailView().getAttachmentView().refresh();
		}
	}

	public JPanel getJPanel() {
		return jp;
	}
	

	public Task getCurrentTask() {
		return view.getCurrentTask();
	}
	
	
	public ArrayList<File> getCurrentAttachmentList() {
		Task currentTask = getCurrentTask();
		return currentTask.getAttachmentsInclParents();
	}
	
	//-------------------------------------------------------------//
	//-------- Methods for handling messages  ---------------------//
	//-------------------------------------------------------------//
	
	
	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
	
	private void setAttachmentList(ArrayList<File> attachments) {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.setData(attachments);
	}
	
	private void refresh() {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.refresh();
	}

	
	
	//-------------------------------------------------------------//
	//-------- Methods for handling popup events ------------------//
	//-------------------------------------------------------------//
	
	
	public void onPopupOpenFileRequested(MouseEvent e) {
		// TODO
//		File current = (File) list.getSelectedValue();
//		try {
//			Desktop.getDesktop().open(current);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}

	public void onPopupDeleteFileRequested(MouseEvent e) {
		// TODO Auto-generated method stub
//		File current = (File) list.getSelectedValue();
//		controller.deleteFileFromCurrentNode(current);
//		AttachmentListModel alm = (AttachmentListModel) list.getModel();
//		alm.refresh();
	}




}
