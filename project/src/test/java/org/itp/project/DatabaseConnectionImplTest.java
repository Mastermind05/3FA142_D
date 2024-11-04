package org.itp.project;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionImplTest extends TestCase {
	private DBConnection dbConnection;
    private Properties properties;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbConnection = new DBConnection();
        properties = new Properties();

        // Simuliere das Laden einer Test-Properties-Datei (hardcodierte Werte)
        properties.setProperty("testuser.db.url", "jdbc:mariadb://localhost:3306/mydatabase");
        properties.setProperty("testuser.db.user", "user");
        properties.setProperty("testuser.db.pw", "password");

        // Simuliere den Systembenutzer
        System.setProperty("user.name", "testuser");
        dbConnection.openConnection(properties);
    }

    public void testOpenConnection() {
        try {
        	
            // Versuche, eine Verbindung zu öffnen
            dbConnection.openConnection(properties);

            // Hole die Verbindung und prüfe, ob sie nicht null ist
            Connection conn = dbConnection.getConnection();
            assertNotNull("Die Verbindung sollte nicht null sein", conn);

            // Überprüfe, ob die Verbindung gültig ist
            assertFalse("Die Verbindung sollte nicht geschlossen sein", conn.isClosed());

            System.out.println("Verbindung erfolgreich hergestellt.");
        } catch (SQLException e) {
            fail("Datenbankverbindung fehlgeschlagen: " + e.getMessage());
        }
    }
    public void testTruncateAllTables() {
        try {
            // Initiale Verbindung prüfen
            Connection conn = dbConnection.getConnection();
            assertNotNull("Connection should be established", conn);
   
            // Die truncateAllTables Methode aufrufen
            dbConnection.truncateAllTables();

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during truncateAllTables test: " + e.getMessage());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // Schließe die Verbindung nach dem Test
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}