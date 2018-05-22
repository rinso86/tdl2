package tdl.plugins.bugzilla;

import org.junit.Test;

import tdl.utils.localFiles.ConfigurationHelper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;



public class BugzillaConnectionTest {
	
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
	public void executeQueryTest() {
		// TODO
	}
	

}
