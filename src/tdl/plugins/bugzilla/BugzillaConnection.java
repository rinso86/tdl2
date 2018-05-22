package tdl.plugins.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.spi.ConnectorProvider;
import org.json.JSONObject;

import tdl.model.Task;



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
	 * Our access client to the bugzilla rest-api.
	 * 
	 * @param bugzillaUrl
	 * @param userEmail
	 * @param userPassword
	 * 
	 * @throws MalformedURLException 
	 */
	public BugzillaConnection(	String bugzillaUrl, String userEmail, String userPassword, 
								String proxyUrl, int proxyPort) throws MalformedURLException {
		
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		
		
		ClientConfig config = new DefaultClientConfig();
	    config.property(ClientProperties.PROXY_URI, proxyUrl + ":" + proxyPort);
	    //config.property(ClientProperties.PROXY_USERNAME,user);
	    //config.property(ClientProperties.PROXY_PASSWORD,pass);
	    this.client = JerseyClientBuilder.newClient(config);
		

	}
	
	
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) throws MalformedURLException {
		
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.client = JerseyClientBuilder.newClient();
		
	}
	

	/**
	 * Let bugzilla know that we have completed a task.
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
	
	/**
	 * http://www.baeldung.com/jersey-jax-rs-client
	 * 
	 * @param requestString
	 * @return
	 */
	public String executeGet(String requestString) {
		WebTarget webTarget = client.target(requestString);
		Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		String output = response.getEntity().toString();
		return output;
	}

}
