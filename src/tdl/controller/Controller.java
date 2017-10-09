package tdl.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;

import tdl.model.MutableTask;
import tdl.model.Task;
import tdl.utils.Analysis;
import tdl.utils.ResourceManager;
import tdl.utils.Savior;
import tdl.messages.Message;
import tdl.messages.MessageType;
import tdl.messages.Recipient;
import tdl.view.overal.OveralView;
import tdl.view.details.DetailView;
import tdl.view.tree.TreeView;
import tdl.view.upcoming.UpcomingView;
import tdl.view.wisecrack.WiseCrackerView;


public class Controller implements Recipient{

	
	private static final String SAVEFILE = "mytree.bin";
	
	// Model
	private MutableTask baseTask;
	private MutableTask currentTask;
	private Date currentTaskActiveSince;
	
	// Utils
	private ResourceManager resourceManager;
	private Savior savior;
	private Analysis analyst;
	
	// Views
	private TreeView treeView;
	private UpcomingView upcomingView;
	private DetailView detailView;
	private WiseCrackerView wiseCrackView;
	private OveralView overalView;


	
	
	public Controller() throws ClassNotFoundException, IOException {
		
		// Model and utils
		resourceManager = new ResourceManager();
		savior = new Savior();
		baseTask = savior.loadTree(SAVEFILE);
		currentTask = baseTask;
		analyst = new Analysis();
		analyst.calculateModelParameters(baseTask);
		currentTaskActiveSince = new Date();
		
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
	
	public int estimateTimeToComplete(Task t) {
		double estimate = analyst.estimateTimeToComplete(t);
		return (int)estimate;
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
		case PREPARE_WINDOW_CLOSING:
			prepareWindowClosing(message);
			break;
		case TASK_CHANGE_TITLE_REQUEST:
			changeTitle(message);
			break;
		case TASK_CHANGE_DEADLINE_REQUEST:
			changeDeadline(message);
			break;
		case MOVE_TASK_REQUEST:
			moveTask(message);
			break;
		case SAVE_TASK_REQUEST:
			// TODO;
			break;
		default:
			break;
		}
	}


	private void moveTask(Message message) {
		Task task = (Task) message.getHeaders().get("task");
		Task oldParent = (Task) message.getHeaders().get("fromParent");
		Task newParent = (Task) message.getHeaders().get("toParent");
		
		saveDetailsToTask();
		
		MutableTask mtask = fetchMutableTask(task);
		MutableTask moldParent = fetchMutableTask(oldParent);
		MutableTask mnewParent = fetchMutableTask(newParent);
		
		System.out.println("Controller now moving " + task.getTitle() + " from " + oldParent.getTitle() + " to " + newParent.getTitle());
		moldParent.deleteChild(mtask);
		mnewParent.addChild(mtask);

		analyst.calculateModelParameters(baseTask);
		
		Message response = new Message(MessageType.MOVED_TASK);
		response.addHeader("task", (Task) mtask);
		response.addHeader("fromParent", (Task) moldParent);
		response.addHeader("toParent", (Task) mnewParent);
		broadcast(response);
	}

	private void changeTitle(Message message) {
		Task t = (Task) message.getHeaders().get("task");
		MutableTask mt = fetchMutableTask(t); 
		String title = (String) message.getHeaders().get("title");
		mt.setTitle(title);
		
		Message response = new Message(MessageType.UPDATED_TASK);
		response.addHeader("task", (Task) mt);
		broadcast(response);
	}
	

	private void changeDeadline(Message message) {
		Task t = (Task) message.getHeaders().get("task");
		MutableTask mt = fetchMutableTask(t);
		Date newDeadline = (Date) message.getHeaders().get("deadline");
		mt.setDeadline(newDeadline);
		
		Message response = new Message(MessageType.UPDATED_TASK);
		response.addHeader("task", (Task) mt);
		broadcast(response);
	}

	private void deleteFile(Message message) {
		File f = (File) message.getHeaders().get("file");
		Task t = (Task) message.getHeaders().get("task");
		String title = t.getTitle();
		MutableTask task = fetchMutableTask(t);
		task.deleteAttachment(f);
		Message response = new Message(MessageType.DELETED_FILE);
		response.addHeader("task", t);
		System.out.println("Controller deleted file " +title+ " from task " + t.getTitle());
		broadcast(response);
	}

	private void reactivateTask(Message message) {
		MutableTask task = fetchMutableTask((Task) message.getHeaders().get("task"));
		task.setCompleted(null);
		Message response = new Message(MessageType.REACTIVATED_TASK);
		response.addHeader("task", (Task) task);
		System.out.println("Controller reactivated task " +task.getTitle());
		broadcast(response);
	}

	private void deleteTask(Message message) {
		
		MutableTask taskToDelete = (MutableTask) message.getHeaders().get("task");
		MutableTask parent = fetchMutableTask(taskToDelete.getParent());
		Message internalMessage = new Message(MessageType.NEW_TASK_ACTIVE_REQUEST);
		internalMessage.addHeader("task", (Task) parent);
		changeCurrentTask(internalMessage);
		
		System.out.println("Tree before delete");
		System.out.println(baseTask.printTree());

		Message response1 = new Message(MessageType.PREPARE_DELETING_TASK);
		response1.addHeader("task", (Task) taskToDelete);
		broadcast(response1);
		
		UUID id = taskToDelete.getId();
		String title = taskToDelete.getTitle();
		parent.deleteChild(taskToDelete);
		
		System.out.println("Tree after delete");
		System.out.println(baseTask.printTree());
		
		Message response2 = new Message(MessageType.DELETED_TASK);
		response2.addHeader("taskId", id);
		System.out.println("Controller deleted task " + title);
		broadcast(response2);
	}

	private void completeTask(Message message) {
		saveDetailsToTask();
		
		MutableTask task = fetchMutableTask((Task) message.getHeaders().get("task"));
		Date currentTime = new Date();
		int currentTaskActivePeriod = (int) Math.floor((currentTime.getTime() - currentTaskActiveSince.getTime())/1000);
		task.incrementSecondsActive(currentTaskActivePeriod);
		task.setCompletedRecursive(new Date());
		
		analyst.calculateModelParameters(baseTask);
		
		Message response = new Message(MessageType.COMPLETED_TASK);
		response.addHeader("task", (Task) task);
		System.out.println("Controller completed task " +task.getTitle());
		broadcast(response);
		
		Message internalMessage = new Message(MessageType.NEW_TASK_ACTIVE_REQUEST);
		internalMessage.addHeader("task", (Task) task.getParent());
		changeCurrentTask(internalMessage);
	}

	private void addSubtask(Message message) {
		saveDetailsToTask();
		
		MutableTask parent = fetchMutableTask((Task) message.getHeaders().get("task"));
		MutableTask child = new MutableTask(parent, "new task");

		analyst.calculateModelParameters(baseTask);
		
		Message response = new Message(MessageType.ADDED_SUBTASK);
		response.addHeader("child", child);
		response.addHeader("parent", parent);
		System.out.println("Controller added child " +child.getTitle()+ " to parent " + parent.getTitle());
		broadcast(response);
		
		Message internalMessage = new Message(MessageType.NEW_TASK_ACTIVE_REQUEST);
		internalMessage.addHeader("task", (Task) child);
		changeCurrentTask(internalMessage);
	}

	private void changeCurrentTask(Message message) {
		MutableTask newCurrentTask = fetchMutableTask((Task) message.getHeaders().get("task"));
		
		if(newCurrentTask == currentTask) {
			System.out.println("Controller does not change active task, because current and new active tasks are the same");
			return; 
		}
		
		System.out.println("Controller changing active task from " + currentTask.getTitle() + " to " + newCurrentTask.getTitle());
		
		Date currentTime = new Date();
		int currentTaskActivePeriod = (int) Math.floor((currentTime.getTime() - currentTaskActiveSince.getTime())/1000);
		currentTask.incrementSecondsActive(currentTaskActivePeriod);
		
		saveDetailsToTask();
		currentTask = newCurrentTask;
		currentTaskActiveSince = currentTime;
		
		Message response = new Message(MessageType.NEW_TASK_ACTIVE);
		response.addHeader("task", (Task) newCurrentTask);
		broadcast(response);
	}
	
	// This is a unfortunate special case, where the controller has to go out to the view and fetch the changes.
	private void saveDetailsToTask() {
		System.out.println("Controller fetches and saves details to task " + currentTask.getTitle() + ": " + detailView.getDescription());
		currentTask.setDescription( detailView.getDescription() );
	}

	private void addFileToTask(Message message) {
		HashMap<String, Object> headers = message.getHeaders();
		File f = (File) headers.get("file");
		Task t = (Task) headers.get("task");
		MutableTask mt = fetchMutableTask(t);
		
		File savedFile = null;
		try {
			savedFile = resourceManager.saveToResources(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mt.addAttachment(savedFile);
		
		Message response = new Message(MessageType.UPDATED_TASK);
		response.addHeader("task", t);
		System.out.println("Controller added file " +f.getName() +" to task " +t.getTitle());
		broadcast(response);
	}

	private void broadcast(Message message) {
		System.out.println("Controller now broadcasting "+ message.getMessageType());
		treeView.receiveMessage(message);
		upcomingView.receiveMessage(message);
		detailView.receiveMessage(message);
		wiseCrackView.receiveMessage(message);
	}

	private MutableTask fetchMutableTask(Task t) {
		return baseTask.searchChildrenUnique(testedTask -> testedTask.getId() == t.getId());
	}
	
	
	private void prepareWindowClosing(Message message) {
		System.out.println("Controller is saving data before shutdown.");
		saveDetailsToTask();
		saveModelToFile();
	}

	private void saveModelToFile() {
		try {
			savior.saveTree(baseTask, SAVEFILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
