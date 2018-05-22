package tdl.plugins.bugzilla;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

import tdl.model.MutableTask;
import tdl.model.Task;
import tdl.utils.network.ProxyConnectionFactory;



/**
 * 
 * https://wiki.mozilla.org/Bugzilla:REST_API
 *
 */

public class BugzillaConnection {

	private URL bugzillaUrl;
	private String userEmail;
	private String userPassword;
	private Client client;

	
	/**
	 * Versichere, dass wir auf die REST API von bugzilla zugreifen können
	 * 
	 * @param bugzillaUrl
	 * @param userEmail
	 * @param userPassword
	 * 
	 * @throws MalformedURLException 
	 */
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword, String proxyUrl, int proxyPort) throws MalformedURLException {
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		URLConnectionClientHandler ch  = new URLConnectionClientHandler(new ProxyConnectionFactory(proxyUrl, proxyPort));
		this.client = new Client(ch);
	}
	
	
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) throws MalformedURLException {
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.client = Client.create();
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
	
	
	public JSONObject executeQuery(HashMap<String, String> paras) {
		// @TODO: 
		
		// Create uri from paras
		// get String by execureGet
		// parse String into JSON object
		return null;
	}
	
	
	public String executeGet(String requestString) {
		WebResource webResource = client.resource(requestString);

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		
		return output;
	}

}
