package tdl.plugins.bugzilla;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import tdl.model.MutableTask;
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
	private CloseableHttpClient httpclient;
	
	/**
	 * Versichere, dass wir auf die REST API von bugzilla zugreifen können
	 * 
	 * @param bugzillaUrl
	 * @param userEmail
	 * @param userPassword
	 * 
	 * @throws MalformedURLException 
	 */
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) throws MalformedURLException {
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.httpclient = HttpClients.createDefault();
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
	
	private JSONObject executeQuery(HashMap<String, String> paras) {
		// @TODO: 
		
		// Create uri from paras
		// get String by execureGet
		// parse String into JSON object
		return null;
	}
	
	private String executeGet(URI request) throws Exception {
		String body = "";
		HttpGet httpget = new HttpGet(request);
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		        long len = entity.getContentLength();
		        if (len != -1) {
		        	body = EntityUtils.toString(entity);
		        } else {
		        	throw new Exception("No response!");
		        }
		    }
		} catch (Exception ex) {
		    response.close();
		    throw ex;
		}
		return body;
	}

}
