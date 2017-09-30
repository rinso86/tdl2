package tdl.controller;

import java.io.IOException;

import javax.swing.JFrame;

import tdl.model.MutableTask;
import tdl.model.Task;
import tdl.utils.ResourceManager;
import tdl.utils.Savior;
import tdl.controller.listeners.OnCloseListener;
import tdl.messages.Message;
import tdl.messages.Recipient;
import tdl.view.overal.OveralView;
import tdl.view.details.DetailView;
import tdl.view.tree.TreeView;
import tdl.view.tree.TaskNode;
import tdl.view.upcoming.UpcomingView;
import tdl.view.wisecrack.WiseCrackerView;


public class Controller implements Recipient{

	
	private static final String SAVEFILE = "mytree.bin";
	
	// Model
	private MutableTask baseTask;
	private MutableTask currentTask;
	
	// Utils
	private ResourceManager resourceManager;
	private Savior savior;
	
	// Views
	private TreeView treeView;
	private UpcomingView upcomingView;
	private DetailView detailView;
	public WiseCrackerView wiseCrackView;
	private OveralView overalView;
	
	
	public Controller() throws ClassNotFoundException, IOException {
		
		// Model and utils
		resourceManager = new ResourceManager();
		savior = new Savior();
		baseTask = savior.loadTree(SAVEFILE);
		currentTask = baseTask;
		
		// Views
		treeView = new TreeView(this);
		upcomingView = new UpcomingView(this);
		detailView = new DetailView(this);
		overalView = new OveralView("My Todo-List", treeView, detailView, upcomingView);
		overalView.setOnCloseListener(new OnCloseListener(this));
		wiseCrackView = new WiseCrackerView(overalView.getJFrame());
	}

	public void run() {
		JFrame f = overalView.getJFrame();
		f.setVisible(true);
	}
	
	public Task getBaseTask() {
		return (Task) baseTask;
	}
	
	public Task getCurrentTask() {
		return (Task) currentTask;
	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
	
	public void saveModelToFile() {
		try {
			savior.saveTree(baseTask, SAVEFILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
