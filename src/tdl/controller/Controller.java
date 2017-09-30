package tdl.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;

import tdl.model.MutableTask;
import tdl.model.Task;
import tdl.utils.ResourceManager;
import tdl.utils.Savior;
import tdl.messages.Message;
import tdl.messages.MessageType;
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
		System.out.println("Controller received " + message.getMessageType());
		switch(message.getMessageType()) {
		case FILE_DROPPED_IN:
			addFileToTask(message);
			break;
		case NEW_TASK_ACTIVE_REQUEST:
			changeCurrentTask(message);
			break;
		case ADD_SUBTASK_REQUEST:
			addSubtask(message);
			break;
		case COMPLETE_TASK_REQUEST:
			completeTask(message);
			break;
		case DELETE_TASK_REQUEST:
			deleteTask(message);
			break;
		case REACTIVATE_TASK_REQUEST:
			reactivateTask(message);
			break;
		case DELETE_FILE_REQUEST:
			deleteFile(message);
			break;
		default:
			break;
		}
	}
	
	private void deleteFile(Message message) {
		File f = (File) message.getHeaders().get("file");
		Task t = (Task) message.getHeaders().get("task");
		MutableTask task = fetchMutableTask(t);
		task.deleteAttachment(f);
		Message response = new Message(MessageType.DELETED_FILE);
		response.addHeader("task", t);
		broadcast(response);
	}

	private void reactivateTask(Message message) {
		MutableTask task = fetchMutableTask((Task) message.getHeaders().get("task"));
		task.setCompleted(null);
		Message response = new Message(MessageType.REACTIVATED_TASK);
		response.addHeader("task", (Task) task);
		broadcast(response);
	}

	private void deleteTask(Message message) {
		MutableTask task = fetchMutableTask((Task) message.getHeaders().get("task"));
		UUID id = task.getId();
		MutableTask parent = task.getParent();
		parent.deleteChild(task);
		
		Message response = new Message(MessageType.DELETED_TASK);
		response.addHeader("taskId", id);
		broadcast(response);
	}

	private void completeTask(Message message) {
		MutableTask task = fetchMutableTask((Task) message.getHeaders().get("task"));
		task.setCompleted(new Date());
		Message response = new Message(MessageType.COMPLETED_TASK);
		response.addHeader("task", (Task) task);
		broadcast(response);
	}

	private void addSubtask(Message message) {
		MutableTask parent = fetchMutableTask((Task) message.getHeaders().get("task"));
		MutableTask child = new MutableTask(parent, "new task");
		Message response = new Message(MessageType.ADDED_SUBTASK);
		broadcast(response);
	}

	private void changeCurrentTask(Message message) {
		MutableTask newCurrentTask = fetchMutableTask((Task) message.getHeaders().get("task"));
		currentTask = newCurrentTask;
		Message response = new Message(MessageType.NEW_TASK_ACTIVE);
		response.addHeader("task", (Task) newCurrentTask);
		broadcast(response);
	}

	private void broadcast(Message message) {
		System.out.println("Controller now broadcasting "+ message.getMessageType());
		treeView.receiveMessage(message);
		upcomingView.receiveMessage(message);
		detailView.receiveMessage(message);
		wiseCrackView.receiveMessage(message);
	}
	
	private void addFileToTask(Message message) {
		HashMap<String, Object> headers = message.getHeaders();
		File f = (File) headers.get("file");
		Task t = (Task) headers.get("task");
		MutableTask mt = fetchMutableTask(t);
		mt.addAttachment(f);
		Message response = new Message(MessageType.UPDATED_TASK);
		broadcast(response);
	}

	private MutableTask fetchMutableTask(Task t) {
		return baseTask.searchChildrenUnique(testedTask -> testedTask.getId() == t.getId());
	}

	public void saveModelToFile() {
		try {
			savior.saveTree(baseTask, SAVEFILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
