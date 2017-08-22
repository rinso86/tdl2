package tdl2.view.detail;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tdl2.controller.Controller;



import org.jdesktop.swingx.JXDatePicker;

public class DetailView {

	private Controller controller;
	private JPanel jp;
	private JLabel descrLabel;
	private JTextField descrTextfield;
	private JXDatePicker deadlinePicker;
	
	public DetailView(Controller controller) {
		this.controller = controller;
		
		descrLabel = new JLabel("Description");
		descrTextfield = new JTextField();
		descrTextfield.setPreferredSize(new Dimension(400, 400));
		
		// http://pirlwww.lpl.arizona.edu/resources/guide/software/SwingX/org/jdesktop/swingx/JXDatePicker.html
		deadlinePicker = new JXDatePicker();
		
//		JPanel dropPanel = new JPanel();
//		FileDrop fileDrop = new FileDrop (dropPanel, new FileDrop.Listener() {
//			
//		});
		
		this.jp = new JPanel(new GridBagLayout());
		GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.NORTHEAST;
        GridBagConstraints bigFieldConstraints = new GridBagConstraints();
        bigFieldConstraints.gridx = 0;
        bigFieldConstraints.gridy = 1;
        bigFieldConstraints.gridwidth = 2;
        GridBagConstraints extraConstraints = new GridBagConstraints();
        extraConstraints.gridx = 1; 
        extraConstraints.gridy = 0;
        
        jp.add(descrLabel, labelConstraints);
        jp.add(descrTextfield, bigFieldConstraints);
        jp.add(deadlinePicker, extraConstraints);
	}

//	public void setOnDescrEditListener(DetailViewOnDescrEditListener detailViewOnDescrEditListener) {
//		// TODO Auto-generated method stub
//		
//	}

	public void setOnDeadlineEditListener(ActionListener l) {
		deadlinePicker.addActionListener(l);
	}
//
//	public void setOnAttachmChangeListener(DetailViewAttachmChangeListener detailViewAttachmChangeListener) {
//		// TODO Auto-generated method stub
//		
//	}

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

}
