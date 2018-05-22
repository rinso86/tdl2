package tdl.plugins.bugzilla;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.client.utils.URIBuilder;
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
	private Proxy proxy;

	
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
		
		this(bugzillaUrl, userEmail, userPassword);
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
		
	}
	
	
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) throws MalformedURLException {
		
		this.bugzillaUrl = new URL(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		
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
	 * Get new tasks that can not yet be found in the task-tree. 
	 * 
	 * @param baseTask
	 */
	public Task[] getNewTasks(Task baseTask) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public JSONObject executeQuery(String path, HashMap<String, String> paras) throws URISyntaxException, IOException {
		
		String fullPath = bugzillaUrl.toString() + path;
		
		// Create uri from paras
		URIBuilder ub = new URIBuilder(fullPath);
		for(String key : paras.keySet()) {
			String value = paras.get(key);
			ub.addParameter(key, value);
		}
		String urlString = ub.build().toString();
		
		// get String by execureGet
		JSONObject jo = executeJsonRestCall(urlString);
		
		return jo;
	}
	
	public JSONObject executeJsonRestCall(String requestString) throws IOException {

		URL url = new URL(requestString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		
		int respCode = connection.getResponseCode();
		if(respCode != 200) throw new IOException("Kein gültiger Aufruf! ResponseCode = " + respCode);
		
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		StringBuilder sb = new StringBuilder();
		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		
		JSONObject jo = new JSONObject(sb.toString());
		
		return jo;
	}
	
	
	/**
	 * @param requestString
	 * @return
	 * @throws IOException 
	 */
	public String executeGet(String requestString) throws IOException {

		URL url = new URL(requestString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("GET");
		int respCode = connection.getResponseCode();
		if(respCode != 200) throw new IOException("Kein gültiger Aufruf! ResponseCode = " + respCode);
		
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		StringBuilder sb = new StringBuilder();
		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		return sb.toString();
		
	}

}
