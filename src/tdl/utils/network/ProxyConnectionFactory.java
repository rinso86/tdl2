package tdl.utils.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

public class ProxyConnectionFactory implements HttpURLConnectionFactory {

	Proxy proxy;

	public ProxyConnectionFactory(String proxyUrl, int proxyPort) {
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));
	}
	
	@Override
	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
		return (HttpURLConnection) url.openConnection(proxy);
	}

}
