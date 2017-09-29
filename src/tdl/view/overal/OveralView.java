package tdl.view.overal;

import java.awt.FlowLayout;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import tdl.view.details.DetailView;
import tdl.view.tree.TreeView;
import tdl.view.upcoming.UpcomingView;

public class OveralView {
	
	private JPanel jp;
	private JFrame jf;
	
	public OveralView (String title, TreeView treeView, DetailView detailView, UpcomingView calendarView) {
		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		
		jp.add(treeView.getPanel());
		jp.add(detailView.getPanel());
		jp.add(calendarView.getPanel());
		
		jf = new JFrame(title);
		jf.setSize(600, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(jp);
		jf.pack();
	}


	public void setOnCloseListener(WindowListener l) {
		jf.addWindowListener(l);
	}

	public JFrame getJFrame() {
		return jf;
	}

}
