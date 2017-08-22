package tdl2.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Task implements Serializable{

	private static final long serialVersionUID = -4314415309059598961L;
	private Task parent;
	private ArrayList<Task> children;
	private UUID id;
	private String title;
	private String description;
	private Date deadline;
	private boolean completed;
	private ArrayList<File> attachments;
	
	public Task() {
		this(null, "");
	}
	
	public ArrayList<Task> getChildren() {
		return children;
	}

	public Task(String title) {
		this(null, title);
	}
	
	public Task(Task parent, String title) {
		this.id = UUID.randomUUID();
		this.parent = parent;
		this.title = title;
		this.description = "";
		this.completed = false;
		this.children = new ArrayList<Task>();
		if(parent != null) {
			parent.registerChild(this);
		}
		this.attachments = new ArrayList<File>();
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
		if(completed) {
			for(Task childTask : children) {
				childTask.setCompleted(true);
			}
		}
	}

	public Task getParent() {
		return parent;
	}

	public void setParent(Task parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
		adjustDeadlineToParent();
	}
	
	public void adjustDeadlineToParent() {
		Task parent = this.getParent();
		if(parent != null) {
			Date parentDeadline = parent.getDeadline();
			if(parentDeadline != null) {
				Date ownDeadline = this.getDeadline();
				long diff = parentDeadline.getTime() - ownDeadline.getTime();
				if (diff < 0) {
					this.setDeadline(parentDeadline);
				}
				for(Task subTask : this.getChildren()) {
					subTask.adjustDeadlineToParent();
				}
			}
		}
	}

	public UUID getId() {
		return id;
	}

	public void addChild(Task subTask) {
		subTask.setParent(this);
		registerChild(subTask);
	}
	
	public void registerChild(Task subTask) {
		children.add(subTask);
	}

	public void deleteChild(Task child) {
		children.remove(child);
	}
	
	public String printTree() {
		String treeString = this.getTitle() + "\n";
		for(Task childTask : this.getChildren()) {
			treeString += this.getTitle() +" --> " + childTask.printTree();
		}
		return treeString;
	}

	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(this);
		for(Task child : this.getChildren()) {
			tasks.addAll(child.getAllTasks());
		}
		return tasks;
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}

	public void addAttachments(ArrayList<File> files) {
		attachments.addAll(files);
	}

	public ArrayList<File> getAttachments() {
		return attachments;
	}
}
