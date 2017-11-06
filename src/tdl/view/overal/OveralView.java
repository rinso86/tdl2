package tdl.view.overal;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tdl.view.details.DetailView;
import tdl.view.tree.TreeView;
import tdl.view.upcoming.UpcomingView;

public class OveralView {
	
	private JPanel jp;
	private JFrame jf;
	
	public OveralView (String title, TreeView treeView, DetailView detailView, UpcomingView upcomingView) {
		
		jp = new JPanel(new GridBagLayout());
		GridBagConstraints tasktreeConstraints = new GridBagConstraints();
		tasktreeConstraints.gridx = 0;
		tasktreeConstraints.gridy = 0;
		tasktreeConstraints.weightx = tasktreeConstraints.weighty = 1.5;
		tasktreeConstraints.fill = GridBagConstraints.BOTH;
        GridBagConstraints detailsConstraints = new GridBagConstraints();
        detailsConstraints.gridwidth = 3;
        detailsConstraints.gridx = 1;
        detailsConstraints.gridy = 0;
        detailsConstraints.weightx = detailsConstraints.weighty = 3.0;
        detailsConstraints.fill = GridBagConstraints.BOTH;
        GridBagConstraints upcomingConstraints = new GridBagConstraints();
        upcomingConstraints.gridwidth = 1;
        upcomingConstraints.gridx = 4; 
        upcomingConstraints.gridy = 0;
        upcomingConstraints.weightx = upcomingConstraints.weighty = 1.0;
        upcomingConstraints.fill = GridBagConstraints.BOTH;
		
        jp.add(treeView.getPanel(), tasktreeConstraints);
        jp.add(detailView.getPanel(), detailsConstraints);
		jp.add(upcomingView.getPanel(), upcomingConstraints);
		
//		jp = new JPanel(new GridBagLayout(1,3));
//      jp.add(treeView.getPanel());
//      jp.add(detailView.getPanel());
//		jp.add(upcomingView.getPanel());
		
		jf = new JFrame(title);
		jf.setSize(800, 600);
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
