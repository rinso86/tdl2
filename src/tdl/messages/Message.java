package tdl.messages;

import java.util.ArrayList;
import java.util.HashMap;

public class Message {

	private MessageType messageType;
	private HashMap<String,Object> headers;
	
	public Message(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public HashMap<String,Object> getHeaders() {
		return headers;
	}
	
	public void addHeader(String key,  Object val) {
		headers.put(key, val);
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
}
