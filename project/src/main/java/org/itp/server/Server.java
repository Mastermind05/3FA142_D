package org.itp.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;

public class Server {
    private static final String BASE_URI = "http://localhost:8080/test/ressources/";
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        startServer();
    }

    public static void startServer() throws IOException {
    	 /*ResourceConfig config = new ResourceConfig()
                 .packages("org.itp.rest")
                 .property(ServerProperties.WADL_FEATURE_DISABLE, true);

        System.out.println("Starting the REST server at " + BASE_URI);
        server = JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
        System.out.println("Bereit für Anfragen..."); */
        new Thread(() -> {
            ResourceConfig config = new ResourceConfig().packages("org.itp.rest");
            System.out.println("Starting the REST server at " + BASE_URI);
            server = JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
            System.out.println("Bereit für Anfragen...");
        }).start();
    }  /*
    public static void main(final String[] args) {
    	final String pack = "org.itp.rest";
    	System.out.println("Start server");
    	System.out.println(BASE_URI);
    	final ResourceConfig rc = new ResourceConfig().packages(pack);
    	server = JdkHttpServerFactory.createHttpServer(
    	URI.create(BASE_URI), rc);
    	System.out.println("Ready for Requests....");
    } */
    public static void stopServer() {
        if (server != null) {
            System.out.println("Stopping the REST server...");
            server.stop(0); // Stoppt den Server sofort (0 Sekunden Verzögerung)
            System.out.println("Server stopped.");
        }
    }
}
