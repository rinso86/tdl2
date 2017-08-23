package tdl2.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JFrame;

import tdl2.controller.detailcontroller.DeadlineSelectListener;
import tdl2.controller.detailcontroller.MyAttachmentListener;
import tdl2.controller.treecontroller.TaskNode;
import tdl2.controller.treecontroller.TaskTreeModelListener;
import tdl2.controller.treecontroller.TaskTreeSelectionListener;
import tdl2.model.Task;
import tdl2.utils.Savior;
import tdl2.view.detail.DetailView;
import tdl2.view.overal.OveralView;
import tdl2.view.tree.TreeView;
import tdl2.view.upcoming.UpcomingView;

public class Controller {
	
	private static final String SAVEFILE = "mytree.bin";
	private Savior savior;
	private Task baseTask;
	private OveralView oview;
	private TreeView treeView;
	private DetailView detailView;
	private UpcomingView calendarView;

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
		detailView.setOnAttachmChangeListener(new MyAttachmentListener(this));
		
		calendarView = new UpcomingView(this);
		
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
	
	public void completeTask(TaskNode node) {
		Task task = node.getTask();
		task.setCompleted(true);
	}
	
	public void reactivateTask(TaskNode node) {
		Task task = node.getTask();
		task.setCompleted(false);
	}

	public void deleteTask(TaskNode node) {
		Task task = node.getTask();
		Task parenttask = task.getParent();
		parenttask.deleteChild(task);
		treeView.removeNode(node);
	}

	public void setDeadlineOnSelectedNode(Date deadline) {
		TaskNode currentnode = treeView.getCurrentNode();
		currentnode.setDeadline(deadline);
	}


	public Task[] getDateSortedTasks() {
		ArrayList<Task> tasklist = baseTask.getAllTasks();
		tasklist = sortByDate(tasklist);
		tasklist = filterByCompletion(tasklist);
		return tasklist.toArray(new Task[0]);
	}
	
	private ArrayList<Task> filterByCompletion(ArrayList<Task> tasklist) {
		ArrayList<Task> filtered = new ArrayList<Task>();
		for(Task t : tasklist) {
			if( ! t.isCompleted() ) {
				filtered.add(t);
			}
		}
		return filtered;
	}

	private ArrayList<Task> sortByDate(ArrayList<Task> tasks) {
		tasks.sort(new Comparator<Task>() {
			@Override
			public int compare(Task task1, Task task2) {
				Date deadline1 = task1.getDeadline();
				Date deadline2 = task2.getDeadline();
				if(deadline1 == null && deadline2 == null) {
					return 0;
				} else if (deadline1 == null) {
					return 1;
				} else if (deadline2 == null) {
					return -1;
				} else {
					return deadline1.compareTo(deadline2);					
				}
			}
		});
		return tasks;
	}
	


	public void deleteFileFromCurrentNode(File file) {
		TaskNode currentnode = treeView.getCurrentNode();
		currentnode.getTask().deleteAttachment(file);
		detailView.setAttachmentList(currentnode.getTask().getAttachments());
	}
	
	

	public DetailView getDetailView() {
		return detailView;
	}

	public UpcomingView getCalendarView() {
		return calendarView;
	}

	public TreeView getTreeView() {
		return treeView;
	}


}
