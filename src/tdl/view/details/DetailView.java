package tdl.view.details;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tdl.controller.Controller;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.model.Task;
import tdl.utils.FileDrop;
import tdl.utils.FileDrop.Listener;
import tdl.view.details.attachments.AttachmentView;

import org.jdesktop.swingx.JXDatePicker;


public class DetailView implements Recipient {

	private Controller controller;
	private JPanel jp;
	private JLabel descrLabel;
	private JTextArea descrTextfield;
	private JScrollPane descrscollpane;
	private JXDatePicker deadlinePicker;
	private JLabel attachLabel;
	private AttachmentView attachmentView;
	private JPanel attachmentListPanel;
	
	
	public DetailView(Controller controller) {
		this.controller = controller;
		
		descrLabel = new JLabel("Description");
		descrTextfield = new JTextArea();
		descrTextfield.setLineWrap(true);
		descrTextfield.setWrapStyleWord(true);
		descrscollpane = new JScrollPane(descrTextfield);
		descrscollpane.setPreferredSize(new Dimension(400, 400));
		
		deadlinePicker = new JXDatePicker();
		
		attachLabel = new JLabel("attachments");
		attachmentView = new AttachmentView(this);
		attachmentListPanel = attachmentView.getJPanel();
		
		
		this.jp = new JPanel(new GridBagLayout());
		GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.NORTHEAST;
        GridBagConstraints bigFieldConstraints = new GridBagConstraints();
        bigFieldConstraints.gridx = 0;
        bigFieldConstraints.gridy = 1;
        bigFieldConstraints.gridwidth = 2;
        GridBagConstraints secondBigFieldConstraints = new GridBagConstraints();
        secondBigFieldConstraints.gridx = 2;
        secondBigFieldConstraints.gridy = 1;
        secondBigFieldConstraints.gridwidth = 1;
        GridBagConstraints extraConstraints = new GridBagConstraints();
        extraConstraints.gridx = 1; 
        extraConstraints.gridy = 0;
        GridBagConstraints secondExtraConstraints = new GridBagConstraints();
        secondExtraConstraints.gridx = 2; 
        secondExtraConstraints.gridy = 0;
        
        jp.add(descrLabel, labelConstraints);
        jp.add(descrscollpane, bigFieldConstraints);
        jp.add(deadlinePicker, extraConstraints);
        jp.add(attachLabel, secondExtraConstraints);
        jp.add(attachmentListPanel, secondBigFieldConstraints);
	}

	public JPanel getPanel() {
		return jp;
	}

	public Task getCurrentTask() {
		return controller.getCurrentTask();
	}
	
	public ArrayList<File> getCurrentAttachmentList() {
		return getCurrentTask().getAttachmentsInclParents();
	}
	
	@Override
	public void receiveMessage(Message message) {
		System.out.println("DetailView now receiving " + message.getMessageType());
		switch(message.getMessageType()) {
		case FILE_DROPPED_IN:
			controller.receiveMessage(message);
			break;
		case UPDATED_TASK:
			setCurrentTask((Task) message.getHeaders().get("task"));
			attachmentView.receiveMessage(message);
			break;
		case NEW_TASK_ACTIVE:
			setCurrentTask((Task) message.getHeaders().get("task"));
			attachmentView.receiveMessage(message);
			break;
		case ADDED_SUBTASK:
			break;
		case DELETE_FILE_REQUEST:
			controller.receiveMessage(message);
			break;
		default:
			break;
		}
	}

	private void setCurrentTask(Task currentTask) {
		setDescription(currentTask.getDescription());
		setDeadline(currentTask.getDeadline());
	}

	private void setDeadline(Date deadline) {
		deadlinePicker.setDate(deadline);
	}


	private void setDescription(String description) {
		descrTextfield.setText(description);
	}

}
