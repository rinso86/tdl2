package tdl2.view.detail;



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

import tdl2.controller.Controller;
import tdl2.utils.FileDrop;
import tdl2.utils.FileDrop.Listener;
import tdl2.view.detail.attachments.AttachmentListModel;
import tdl2.view.detail.attachments.AttachmentView;

import org.jdesktop.swingx.JXDatePicker;

public class DetailView {

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
		attachmentView = new AttachmentView(controller);
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

//	public void setOnDescrEditListener(DetailViewOnDescrEditListener detailViewOnDescrEditListener) {
//		// TODO Auto-generated method stub
//		
//	}

	public void setOnDeadlineEditListener(ActionListener l) {
		deadlinePicker.addActionListener(l);
	}

	public void setOnAttachmChangeListener(Listener listener) {
		attachmentView.setOnAttachmChangeListener(listener);
	}

	public JPanel getPanel() {
		return jp;
	}

	public String getDescription() {
		return descrTextfield.getText();
	}

	public void setDescription(String text) {
		descrTextfield.setText(text);
	}

	public void setDeadline(Date deadline) {
		deadlinePicker.setDate(deadline);
	}

	public Date getDeadline() {
		return deadlinePicker.getDate();
	}

	public void setAttachmentList(ArrayList<File> attachments) {
		attachmentView.setAttachmentList(attachments);
	}
	
	public AttachmentView getAttachmentView() {
		return attachmentView;
	}

}
