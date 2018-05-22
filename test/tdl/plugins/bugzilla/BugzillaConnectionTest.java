package tdl.plugins.bugzilla;

import org.json.JSONObject;
import org.junit.Test;

import tdl.utils.localFiles.ConfigurationHelper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Properties;



public class BugzillaConnectionTest {
	
	@Test
	public void testInternetWOProxy() throws MalformedURLException, IOException {
		
		URL url = new URL("https://www.google.com");
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		int respCode = connection.getResponseCode();
		
		assertTrue(respCode != 200);
	}
	
	@Test
	public void testInternet() throws MalformedURLException, IOException {
		
		URL url = new URL("https://www.google.com");
		
		Properties props = ConfigurationHelper.loadProperties();
		String proxyUrl = props.getProperty("proxy.url");
		int proxyPort = Integer.parseInt(props.getProperty("proxy.port"));
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("GET");
		int respCode = connection.getResponseCode();
		
		assertTrue(respCode == 200);
	}
	
	@Test
	public void executeGetTest() throws Exception  {
		Properties props = ConfigurationHelper.loadProperties();
		BugzillaConnection conn = new BugzillaConnection(
				props.getProperty("bugzilla.url"), 
				props.getProperty("bugzilla.user"), 
				props.getProperty("bugzilla.password"), 
				props.getProperty("proxy.url"), 
				Integer.parseInt(props.getProperty("proxy.port")) 
		);
		String body = conn.executeGet("https://www.google.com");
		assertNotNull(body);
		assertTrue(body != "");
	}

	@Test
	public void executeQueryTest() throws URISyntaxException, IOException {
		
		Properties props = ConfigurationHelper.loadProperties();
		
		BugzillaConnection conn = new BugzillaConnection(
				"https://bugzilla.mozilla.org", 
				"", 
				"", 
				props.getProperty("proxy.url"), 
				Integer.parseInt(props.getProperty("proxy.port")) 
		);
		
		String path = "/rest/bug/35";
		HashMap<String, String> paras = new HashMap<String, String>();
		paras.put("include_fields", "summary,status,resolution");
		JSONObject response = conn.executeQuery(path, paras);
		
		assertNotNull(response);
	}
	

}
