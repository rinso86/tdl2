package tdl.plugins.bugzilla;

import tdl.model.MutableTask;
import tdl.model.Task;

public class BugzillaConnection {

	/**
	 * Versichere, dass wir auf die REST API von bugzilla zugreifen können
	 * 
	 * @param bugzillaUrl
	 * @param userEmail
	 * @param userPassword
	 */
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Schließe eine Aufgabe in bugzilla ab
	 * 
	 * @param bugzillaId
	 * @param description
	 */
	public void complete(int bugzillaId, String description) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Get new tasks that can not yet be found in the tasktree. 
	 * 
	 * @param baseTask
	 */
	public Task[] getNewTasks(Task baseTask) {
		// TODO Auto-generated method stub
		return null;
	}

}
