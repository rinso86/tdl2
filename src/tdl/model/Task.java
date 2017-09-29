package tdl.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public interface Task {

	public Task getParent();
	
	public ArrayList<Task> getChildren();

	public UUID getId();

	public String getTitle();

	public String getDescription();

	public boolean isCompleted();

	public Date getDeadline();

	public ArrayList<File> getAttachments();

	public ArrayList<File> getAttachmentsInclParents();
	
	public long getSecondsActive();

	public String printTree();
	
	
}
