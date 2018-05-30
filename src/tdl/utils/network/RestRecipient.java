package tdl.utils.network;

import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

public interface RestRecipient {

	public void handleRestResponse (JSONObject response, URL path, HashMap<String, String> paras);
	
}
