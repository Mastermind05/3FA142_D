package org.itp.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Properties;

public class Server {
    private static final String BASE_URI = "http://localhost:8080/test/ressources/";
    private static HttpServer server;
    private static DBConnection dbConnection = new DBConnection();
    private static Properties properties;

    public static void main(String[] args) {
        try {
            // Starte die Datenbankverbindung und den Server
            startServer(BASE_URI);
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startServer(String baseUri) throws IOException {
        dbConnection = new DBConnection();
        properties = new Properties();

        // Lade die Verbindung zur Datenbank
        try {
            InputStream input = Server.class.getClassLoader().getResourceAsStream("credentials.properties");
            if (input == null) {
                throw new IOException("credentials.properties not found");
            }
            properties.load(input);

            dbConnection.openConnection(properties);
            dbConnection.createAllTables();
            @SuppressWarnings("unused")
			SQLStatement sqlStatement = new SQLStatement(dbConnection);

            System.out.println("Datenbankverbindung erfolgreich hergestellt und Tabellen erstellt.");
        } catch (IOException | SQLException e) {
            System.err.println("Fehler bei der Datenbankverbindung: " + e.getMessage());
            e.printStackTrace();
            return; // Server nicht starten, wenn die Datenbankverbindung fehlschlägt
        }

        // Starte den REST-Server in einem separaten Thread
        new Thread(() -> {
            ResourceConfig config = new ResourceConfig().packages("org.itp.rest");
            System.out.println("Starting the REST server at " + baseUri);
            server = JdkHttpServerFactory.createHttpServer(URI.create(baseUri), config);
            System.out.println("Bereit für Anfragen...");
        }).start();
    }


    public static void stopServer() {
        if (server != null) {
            System.out.println("Stopping the REST server...");
            server.stop(0); // Stoppt den Server sofort (0 Sekunden Verzögerung)
            System.out.println("Server stopped.");
        }

        // Datenbankverbindung schließen
        if (dbConnection != null) {
            dbConnection.closeConnection();
			System.out.println("Datenbankverbindung geschlossen.");
        }
    }
}
