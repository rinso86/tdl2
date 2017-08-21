package tdl2.model;

import java.io.Serializable;
import java.util.ArrayList;
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
		this.children = new ArrayList<Task>();
		if(parent != null) {
			parent.registerChild(this);
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
}
