package tdl2.view.detail;



import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tdl2.controller.Controller;

public class DetailView {

	private Controller controller;
	private JPanel jp;
	private JLabel descrLabel;
	private JTextField descrTextfield;
	
	public DetailView(Controller controller) {
		this.controller = controller;
		
		descrLabel = new JLabel("description");
		descrTextfield = new JTextField();
		descrTextfield.setPreferredSize(new Dimension(400, 400));
		
		this.jp = new JPanel();
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
	}

//	public void setOnDescrEditListener(DetailViewOnDescrEditListener detailViewOnDescrEditListener) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setOnDeadlineEditListener(DetailViewOnDeadlineEditListener detailViewOnDeadlineEditListener) {
//		// TODO Auto-generated method stub
//		
//	}
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

}
