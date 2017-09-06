package tdl2.view.upcoming;



import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tdl2.controller.Controller;
import tdl2.model.Task;

public class UpcomingView {

	private Controller controller;
	private JPanel jp;
	private JLabel listlabel;
	private JList<Task> jlist;
	private JScrollPane listscrollpane;
	
	public UpcomingView(Controller controller) {
		
		this.controller = controller;
		
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
		
		listlabel = new JLabel("Upcoming");
		jp.add(listlabel, labelConstraints);
		
		Task[] tasks = controller.getDateSortedTasks();
		jlist = new JList<Task>(new TaskListModel(tasks));
		jlist.setCellRenderer(new TaskListRenderer());
		listscrollpane = new JScrollPane(jlist);
		listscrollpane.setPreferredSize(new Dimension(400, 400));
		jp.add(listscrollpane, bigFieldConstraints);
	}

	public JPanel getPanel() {
		return jp;
	}
	
	public void updateList() {
		Task[] tasks = controller.getDateSortedTasks();
		TaskListModel model = (TaskListModel) jlist.getModel();
		model.setData(tasks);
	}
	
	public void refreshView() {
		TaskListModel model = (TaskListModel) jlist.getModel();
		model.refreshView();
	}

}
