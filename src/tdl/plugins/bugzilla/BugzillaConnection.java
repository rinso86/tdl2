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
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
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
	private String token;
	private Proxy proxy;

	
	/**
	 * Our access client to the bugzilla rest-api.
	 * 
	 * @param bugzillaUrl
	 * @param userEmail
	 * @param userPassword
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public BugzillaConnection(	String bugzillaUrl, String userEmail, String userPassword, 
								String proxyUrl, int proxyPort) throws URISyntaxException, IOException {
		
		this(bugzillaUrl, userEmail, userPassword);
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
		
	}
	
	
	public BugzillaConnection(String bugzillaUrl, String userEmail, String userPassword) throws URISyntaxException, IOException {
		
		this(bugzillaUrl);
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.token = authenticateGetToken(userEmail, userPassword);
		
	}
	
	public BugzillaConnection(String bugzillaUrl) throws MalformedURLException {		
		this.bugzillaUrl = new URL(bugzillaUrl);
	}
	
	public String authenticateGetToken(String userMail, String userPassword) throws URISyntaxException, IOException {
		String path = "/rest/login";
		HashMap<String, String> paras = new HashMap<String, String>();
		paras.put("login", userMail);
		paras.put("password", userPassword);
		JSONObject response = executeQuery(path, paras);
		String token = response.getString("token");
		return token;
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
	 * 
	 * @param baseTask
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public ArrayList<Task> getTasks() throws URISyntaxException, IOException {

		ArrayList<Task> tasks = new ArrayList<Task>();
		
		String path = "/rest/bug";
		HashMap<String, String> paras = new HashMap<String, String>();
		paras.put("assigned_to", userEmail);
		JSONObject response = executeQuery(path, paras);
		
		JSONArray bugs = response.getJSONArray("bugs");
		for(int i = 0; i < bugs.length(); i++) {
			
			JSONObject bug = bugs.getJSONObject(i);
			int id = bug.getInt("id");
			String title = bug.getString("summary");
			String description = bug.getString("description");
			
			BugzillaTask bt = new BugzillaTask(this, id, title);
			bt.setDescription(description);
			
			tasks.add(bt);
		}
		
		return tasks;
	}
	
	
	public JSONObject executeQuery(String path, HashMap<String, String> paras) throws URISyntaxException, IOException {
		
		String fullPath = bugzillaUrl.toString() + path;
		
		// Create uri from paras
		URIBuilder ub = new URIBuilder(fullPath);
		for(String key : paras.keySet()) {
			String value = paras.get(key);
			ub.addParameter(key, value);
		}
		if(token != null) ub.addParameter("token", token);
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
