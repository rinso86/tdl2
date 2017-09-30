package tdl.messages;

import java.util.ArrayList;
import java.util.HashMap;

public class Message {

	private HashMap<String,Object> headers;
	
	public Message() {}
	
	public HashMap<String,Object> getHeaders() {
		return headers;
	}
	
	public void addHeader(String key,  Object val) {
		headers.put(key, val);
	}
	
}
