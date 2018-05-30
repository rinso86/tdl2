package tdl.utils.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.naming.TimeLimitExceededException;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;


/**
 * RestUtils is perfectly fine for any short REST Queries you may have. 
 * But when you have long running operations, it makes sense to out-source 
 * these tasks to a separate thread. This is what this class is for.
 *
 */
public class RestThread extends Thread {
	
	
	
	private class RestRequest {
		
		public URL path; 
		public HashMap<String, String> paras; 
		public RestRecipient recipient;
		
		public RestRequest(URL path, HashMap<String, String> paras, RestRecipient responseHandler) {
			this.path = path;
			this.paras = paras;
			this.recipient = responseHandler;
		}
		
	}
	
	
	
	private boolean alive;
	private Proxy proxy;
	private LinkedBlockingQueue<RestRequest> queue;
	private long sleepTimeMilis;
	
	public RestThread() {
		this.sleepTimeMilis = 1000;
		this.queue = new LinkedBlockingQueue<RestRequest>();
	}
	
	public void setProxy (String proxyUrl, int proxyPort) {
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
	}
	
	public void setSleepTimeMilis(long stm) {
		sleepTimeMilis = stm;
	}
	
	public void enqueueQuery(URL path, HashMap<String, String> paras, RestRecipient recipient) throws InterruptedException, TimeLimitExceededException {
		RestRequest rr = new RestRequest(path, paras, recipient);
		boolean success = queue.offer(rr, 1, TimeUnit.SECONDS);
		if(!success) {
			throw new TimeLimitExceededException("Could not add the request " + path + " to the queue.");
		}
	}
	
	@Override
	public void run() {
		while(alive) {
			
			RestRequest rr = queue.poll();
			if(rr != null) {
				
				try {
					JSONObject jo = RestUtils.executeQuery(proxy, rr.path, rr.paras);
					rr.recipient.handleRestResponse(jo, rr.path, rr.paras);
				} catch (URISyntaxException | IOException e) {
					e.printStackTrace();
				}
				
			} else {
				try {
					Thread.sleep(sleepTimeMilis);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	public void startRestThread() {
		alive = true;
		this.start();
	}
	
	public void killRestThread() {
		alive = false;
	}
	
	@Override
	protected void finalize() throws Throwable {
		killRestThread();
		super.finalize();
	}


}
