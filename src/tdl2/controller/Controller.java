package tdl2.controller;

import java.io.IOException;

import javax.swing.JFrame;

import tdl2.controller.detailcontroller.DeadlineSelectListener;
import tdl2.controller.treecontroller.TaskNode;
import tdl2.controller.treecontroller.TaskTreeModelListener;
import tdl2.controller.treecontroller.TaskTreeSelectionListener;
import tdl2.model.Task;
import tdl2.utils.Savior;
import tdl2.view.calendar.CalendarView;
import tdl2.view.detail.DetailView;
import tdl2.view.overal.OveralView;
import tdl2.view.tree.TreeView;

public class Controller {
	
	private static final String SAVEFILE = "mytree.bin";
	private Savior savior;
	private Task baseTask;
	private OveralView oview;
	private TreeView treeView;
	private DetailView detailView;
	private CalendarView calendarView;

	public Controller() throws ClassNotFoundException, IOException {
		savior = new Savior();
		baseTask = savior.loadTree(SAVEFILE);
		
		TaskNode tn = new TaskNode(baseTask);
		treeView = new TreeView(this, tn);
		treeView.setOnFocusChangeListener(new TaskTreeSelectionListener(this));
		treeView.setOnStructureChangeListener(new TaskTreeModelListener(this));
		
		detailView = new DetailView(this);
//		detailView.setOnDescrEditListener(null); <-- we already do this on the tasktreeselectionlistener. 
		detailView.setOnDeadlineEditListener(new DeadlineSelectListener(this));
//		detailView.setOnAttachmChangeListener(null);
		
		calendarView = new CalendarView(this);
		
		oview = new OveralView("My Todo-List", treeView, detailView, calendarView);
		oview.setOnCloseListener(new OnCloseListener(this));
	}
	
	public void run() {
		JFrame f = oview.getJFrame();
		f.setVisible(true);
	}

	public void saveModelToFile() {
		try {
			savior.saveTree(baseTask, SAVEFILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addNewSubtask(TaskNode node) {
		Task task = node.getTask();
		Task subtask = new Task("Set title here");
		task.addChild(subtask);
		TaskNode subnode = new TaskNode(subtask);
		treeView.addChild(node, subnode);
	}

	public void deleteTask(TaskNode node) {
		Task task = node.getTask();
		Task parenttask = task.getParent();
		parenttask.deleteChild(task);
		treeView.removeNode(node);
	}

	public DetailView getDetailView() {
		return detailView;
	}

}
