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
import tdl.messages.MessageType;
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
	@SuppressWarnings("unused")
	private FileDrop fileDrop;
	private JList<File> attachmentList;
	private JScrollPane attchscrollpane;

	public AttachmentView(DetailView view) {
		this.view = view;
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
			for(File file : files) {
				Message m = new Message(MessageType.FILE_DROPPED_IN);
				m.addHeader("file", file);
				view.receiveMessage(m);
			}
		}
	}

	public JPanel getJPanel() {
		return jp;
	}
	

	public Task getCurrentTask() {
		return view.getCurrentTask();
	}	

	
	//-------------------------------------------------------------//
	//-------- Methods for handling messages  ---------------------//
	//-------------------------------------------------------------//
	
	
	@Override
	public void receiveMessage(Message message) {
		switch(message.getMessageType()) {
		case UPDATED_TASK:
			Task currentTask = (Task) message.getHeaders().get("task");
			setAttachmentList(currentTask.getAttachmentsInclParents());
			break;
		case NEW_TASK_ACTIVE:
			Task newCurrentTask = (Task) message.getHeaders().get("task");
			setAttachmentList(newCurrentTask.getAttachmentsInclParents());
			break;
		}
	}
	
	private void setAttachmentList(ArrayList<File> attachments) {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.setData(attachments);
		refresh();
	}
	
	private ArrayList<File> getCurrentAttachmentList() {
		return view.getCurrentAttachmentList();
	}
	
	private void refresh() {
		AttachmentListModel alm = (AttachmentListModel) attachmentList.getModel();
		alm.refresh();
	}

	
	
	//-------------------------------------------------------------//
	//-------- Methods for handling popup events ------------------//
	//-------------------------------------------------------------//
	
	
	public void onPopupOpenFileRequested(MouseEvent e) {
		File fileToOpen = attachmentList.getModel().getElementAt(attachmentList.locationToIndex(e.getPoint()));
		try {
			Desktop.getDesktop().open(fileToOpen);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void onPopupDeleteFileRequested(MouseEvent e) {
		File file = attachmentList.getModel().getElementAt(attachmentList.locationToIndex(e.getPoint()));
		Message m = new Message(MessageType.DELETE_FILE_REQUEST);
		m.addHeader("file", file);
		m.addHeader("task", view.getCurrentTask());
		view.receiveMessage(m);
	}




}