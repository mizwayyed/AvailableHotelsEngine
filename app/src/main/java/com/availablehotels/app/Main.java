package com.availablehotels.app;

import java.io.IOException;
import java.net.URI;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.availablehotels.app.services.commons.AVHotelsLogger;

/**
 * Main class.
 *
 */
public class Main {
	// Base URI the  HTTP server will listen on
	private static ResourceBundle serverConfig = ResourceBundle.getBundle("ServerConfig");

	//    public static final String BASE_URI = "http://localhost:9001/myapp/";
	public static final String BASE_URI = serverConfig.getString("BASE_URI");

	public static final Logger avHotelLogger = AVHotelsLogger.getLogger();

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.availablehotels.app package
		final ResourceConfig rc = new ResourceConfig().packages("com.availablehotels.app.services","com.availablehotels.app.dummyservices");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Main method.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		HttpServer server = null;
		try {
			server = startServer();
			avHotelLogger.info(String.format("Available Hotels app started with WADL available at "
					+ "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
			System.in.read();

		} finally {
			if (server != null) {	
				server.stop();
			}
		}
	}
}

