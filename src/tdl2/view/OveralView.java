package tdl2.view;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tdl2.controller.Controller.OviewOnCloseListener;

public class OveralView {
	
	private String title;
	private JPanel jp;
	private JFrame jf;
	
	public OveralView (String title) {
		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jf = new JFrame(title);
		jf.setSize(600, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setOnCloseListener(OviewOnCloseListener oviewOnCloseListener) {
		
	}

	public JFrame getJFrame() {
		return jf;
	}

}
