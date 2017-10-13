package tdl.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.function.Predicate;

public class MutableTask implements Serializable, Task {

	private static final long serialVersionUID = -4314415309059598961L;
	private MutableTask parent;
	private ArrayList<MutableTask> children;
	private UUID id;
	private String title;
	private String description;
	private Date deadline;
	private Date completed;
	private long secondsActive;
	private ArrayList<File> attachments;
	
	public MutableTask() {
		this(null, "");
	}
	
	public MutableTask(String title) {
		this(null, title);
	}
	
	public MutableTask(MutableTask parent, String title) {
		this.id = UUID.randomUUID();
		this.parent = parent;
		this.title = title;
		this.description = "";
		this.completed = null;
		this.children = new ArrayList<MutableTask>();
		this.attachments = new ArrayList<File>();
		this.secondsActive = 0;
		if(parent != null) {
			parent.registerChild(this);
		}
	}
	
	public ArrayList<MutableTask> getMutableChildren() {
		return children;
	}
	
	@Override
	public ArrayList<Task> getChildren() {
		ArrayList<Task> children = new ArrayList<Task>();
		for(Task child : getMutableChildren()) {
			children.add(child);
		}
		return children;
	}

	public boolean isCompleted() {
		return (completed != null);
	}

	public Date getCompleted() {
		return completed;
	}
	
	public void setCompleted(Date completed) {
		this.completed = completed;
	}
	
	public void setCompletedRecursive(Date completed) {
		if(!isCompleted()) {
			this.completed = completed;			
		}
		for(MutableTask childTask : children) {
			childTask.setCompletedRecursive(completed);
		}
	}

	public MutableTask getParent() {
		return parent;
	}

	public void setParent(MutableTask parent) {
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
		setDeadlineRecursive(deadline);
	}
	
	public void setDeadlineRecursive(Date deadline) {
		if(!this.isCompleted()) {
			if(this.getDeadline() == null) {
				this.deadline = deadline;
			}
			else if( this.getDeadline().after(deadline) ) {
				this.deadline = deadline;
			}			
		}
		for(MutableTask child : this.getMutableChildren()) {
			child.setDeadlineRecursive(deadline);
		}
	}
	
	public void adjustDeadlineToParent() {
		MutableTask parent = this.getParent();
		if(parent != null) {
			Date parentDeadline = parent.getDeadline();
			if(parentDeadline != null) {
				Date ownDeadline = this.getDeadline();
				long diff = parentDeadline.getTime() - ownDeadline.getTime();
				if (diff < 0) {
					this.setDeadline(parentDeadline);
				}
				for(MutableTask subTask : this.getMutableChildren()) {
					subTask.adjustDeadlineToParent();
				}
			}
		}
	}

	public UUID getId() {
		return id;
	}

	public void addChild(MutableTask subTask) {
		subTask.setParent(this);
		registerChild(subTask);
	}
	
	public void registerChild(MutableTask subTask) {
		children.add(subTask);
	}

	public void deleteChild(MutableTask child) {
		children.remove(child);
	}
	
	public String printTree() {
		ArrayList<String> list = printTreeList();
		String string = String.join("\n", list);
		return string;
	}
	
	public ArrayList<String> printTreeList() {
		ArrayList<String> list = new ArrayList<String>();
		
		list.add(this.getTitle());
		
		for(MutableTask child : this.getMutableChildren()) {
			ArrayList<String> sublist = child.printTreeList();
			for(String s : sublist) {
				list.add("    " + s);
			}
		}
		
		return list;
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}
	

	public void addAttachment(File f) {
		attachments.add(f);
	}

	public void addAttachments(ArrayList<File> files) {
		for(File file : files) {
			addAttachment(file);
		}
	}

	public ArrayList<File> getAttachments() {
		return attachments;
	}
	
	public ArrayList<File> getAttachmentsInclParents() {
		ArrayList<File> atchs = new ArrayList<File>();
		atchs.addAll(attachments);
		if(this.parent != null) {
			atchs.addAll(this.parent.getAttachmentsInclParents());
		}
		return atchs;
	}

	public void deleteAttachment(File file) {
		attachments.remove(file);
	}

	public void setAttachments(ArrayList<File> arrayList) {
		attachments = arrayList;
	}
	
	public void incrementSecondsActive(long sec) {
		if(!isCompleted()) {
			this.secondsActive += sec;			
		}
	}
	
	public long getSecondsActive() {
		return this.secondsActive;
	}

	public long getSecondsActiveRecursive() {
		long secs = this.secondsActive;
		for(Task child : getChildren()) {
			secs += child.getSecondsActiveRecursive();
		}
		return secs;
	}
	
	public ArrayList<Task> searchChildren(Predicate<Task> pred) {
		ArrayList<Task> foundTasks = new ArrayList<Task>();
		if(pred.test(this)) {
			foundTasks.add(this);
		}else {
			for(Task child : getChildren()) {
				ArrayList<Task> subResults = child.searchChildren(pred);
				if(subResults.size() > 0) {
					foundTasks.addAll(subResults);
				}
			}
		}
		return foundTasks;
	}
	
	public Task searchChildrenUnique(Predicate<Task> pred) {
		Task foundTask = null;
		if(pred.test(this)) {
			return this;
		}else {
			for(Task child : getChildren()) {
				Task subResult = child.searchChildrenUnique(pred);
				if(subResult != null) {
					foundTask = subResult;
					break;
				}
			}
		}
		return foundTask;
	}

}
