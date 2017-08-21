package tdl2.controller;

import javax.swing.JFrame;

import tdl2.model.Task;
import tdl2.utils.Savior;
import tdl2.view.CalendarView;
import tdl2.view.DetailView;
import tdl2.view.OveralView;
import tdl2.view.TreeView;

public class Controller {
	
	private Savior savior;
	private Task baseTask;
	private OveralView oview;
	private TreeView treeView;
	private DetailView detailView;
	private CalendarView calendarView;

	public Controller() {
		savior = new Savior();
		baseTask = savior.loadTree("mytree.txt");
		
		treeView = new TreeView();
		treeView.setOnFocusChangeListener(new TreeViewOnFocusChangeListener());
		treeView.setOnStructureChangeListener(new TreeViewOnStructureChangeListener());
		
		detailView = new DetailView();
		detailView.setOnDescrEditListener(new DetailViewOnDescrEditListener());
		detailView.setOnDeadlineEditListener(new DetailViewOnDeadlineEditListener());
		detailView.setOnAttachmChangeListener(new DetailViewAttachmChangeListener());
		
		calendarView = new CalendarView();
		
		oview = new OveralView(treeView, detailView, calendarView);
		oview.setOnCloseListener(new OviewOnCloseListener());
	}
	
	public void run() {
		JFrame f = oview.getJFrame();
		f.setVisible(true);
	}
	
	//@todo: make all these interfaces in the views. Also, leave here a implementation of the interfaces
	class TreeViewOnFocusChangeListener {
		
	}
	
	class TreeViewOnStructureChangeListener {
		
	}
	
	class DetailViewOnDescrEditListener {
		
	}
	
	class DetailViewOnDeadlineEditListener {
		
	}
	
	class DetailViewAttachmChangeListener {
		
	}
	
	class OviewOnCloseListener {
		
	}
}
