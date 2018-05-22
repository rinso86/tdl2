package tdl.plugins.bugzilla;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import tdl.model.MutableTask;
import tdl.model.TimeSpan;

public class BugzillaTask extends MutableTask {
	
	private BugzillaConnection bugzillaConnection;
	private int bugzillaId;
	
	public BugzillaTask(BugzillaConnection bugzillaConnection, int bugzillaId, MutableTask parent, String title) {
		super(parent, title);
		this.bugzillaConnection = bugzillaConnection;
		this.bugzillaId = bugzillaId;
	}

	@Override
	public void setCompleted(Date completed) {
		bugzillaConnection.complete(bugzillaId, getDescription());
		super.setCompleted(completed);
	}
	
}
