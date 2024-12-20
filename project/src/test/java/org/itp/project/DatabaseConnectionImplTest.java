package org.itp.project;

import org.itp.dto.Customer;
import org.itp.enums.Gender;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class DatabaseConnectionImplTest {

    private DBConnection dbConnection;
    private Properties properties;
    private SQLStatement sqlStatement;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        dbConnection = new DBConnection();
        properties = new Properties();
        // Initialisiere sqlStatement mit der geöffneten dbConnection
        sqlStatement = new SQLStatement(dbConnection);
        try {
			dbConnection.openConnection(getTestProperties());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        testCustomer = new Customer(
                UUID.randomUUID(),
                "Max",
                "Mustermann",
                LocalDate.of(1990, 1, 1),
                Gender.M
        );
    }
    
    private Properties getTestProperties() {
        Properties properties = new Properties();
        properties.setProperty("testuser.db.url", "jdbc:mariadb://localhost:3306/test");
        properties.setProperty("testuser.db.user", "root");
        properties.setProperty("testuser.db.pw", "password");
        System.setProperty("user.name", "testuser");
        return properties;
    }

    @Test
    public void testOpenConnection() {
        try {
            // Attempt to open a connection
            dbConnection.openConnection(properties);

            // Retrieve the connection and verify it's not null
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "The connection should not be null");

            // Verify that the connection is valid
            assertFalse(conn.isClosed(), "The connection should not be closed");

            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() {
        try {
            dbConnection.openConnection(properties);
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "Connection should be established");

            // Now close the connection and verify it's closed
            dbConnection.closeConnection();
            assertTrue(conn.isClosed(), "The connection should be closed after calling closeConnection");

            System.out.println("Connection closed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during testCloseConnection: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateAllTables() {
        try {
            // Open the connection to the test database
            dbConnection.openConnection(properties);
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "Connection should be established");

            // Call the method to create tables
            dbConnection.createAllTables();

            // Verify the tables were created by checking if they exist
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Check if the 'CUSTOMERS' table exists
            ResultSet rs = metaData.getTables(null, null, "customers", null);
            assertTrue(rs.next(), "The 'customers' table should exist");

            // Check if the 'READING' table exists
            rs = metaData.getTables(null, null, "reading", null);
            assertTrue(rs.next(), "The 'reading' table should exist");
            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during the test: " + e.getMessage());
        }
    }
    

    @Test
    public void testTruncateAllTables() {
        try {
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "Connection should be established");

            // Call the truncateAllTables method
            dbConnection.truncateAllTables();

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during truncateAllTables test: " + e.getMessage());
        }
    }
    @Test
    public void testCreateCustomer() {
        try {
            sqlStatement.createCustomer(testCustomer);

            // Kunden aus der Datenbank abrufen und überprüfen
            Customer retrievedCustomer = sqlStatement.getCustomer(testCustomer.getId());
            assertNotNull(retrievedCustomer, "Der Kunde sollte erfolgreich erstellt worden sein");
            assertEquals(testCustomer.getFirstName(), retrievedCustomer.getFirstName());
            assertEquals(testCustomer.getLastName(), retrievedCustomer.getLastName());
        } catch (SQLException e) {
            fail("Fehler beim Erstellen des Kunden: " + e.getMessage());
        }
    }

    @Test
    public void testGetCustomer() {
        try {
            sqlStatement.createCustomer(testCustomer);

            Customer retrievedCustomer = sqlStatement.getCustomer(testCustomer.getId());
            assertNotNull(retrievedCustomer, "Der Kunde sollte gefunden werden");
            assertEquals(testCustomer.getFirstName(), retrievedCustomer.getFirstName());
        } catch (SQLException e) {
            fail("Fehler beim Abrufen des Kunden: " + e.getMessage());
        }
    }

    @Test
    public void testGetCustomers() {
        try {
            sqlStatement.createCustomer(testCustomer);

            List<Customer> customers = sqlStatement.getCustomers();
            assertFalse(customers.isEmpty(), "Die Kundenliste sollte nicht leer sein");
        } catch (SQLException e) {
            fail("Fehler beim Abrufen der Kundenliste: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomer() {
        try {
            sqlStatement.createCustomer(testCustomer);

            // Kundeninformationen aktualisieren
            testCustomer.setFirstName("Maxi");
            int rowsAffected = sqlStatement.updateCustomer(testCustomer);
            assertEquals(1, rowsAffected, "Genau eine Zeile sollte aktualisiert worden sein");

            // Aktualisierten Kunden abrufen und prüfen
            Customer updatedCustomer = sqlStatement.getCustomer(testCustomer.getId());
            assertEquals("Maxi", updatedCustomer.getFirstName(), "Der Vorname sollte aktualisiert worden sein");
            sqlStatement.deleteCustomer(testCustomer.getId());
        } catch (SQLException e) {
            fail("Fehler beim Aktualisieren des Kunden: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCustomer() {
        try {
            sqlStatement.createCustomer(testCustomer);
            dbConnection.getConnection().commit();

            // Überprüfen, ob der Kunde erstellt wurde
            Customer retrievedCustomer = sqlStatement.getCustomer(testCustomer.getId());
            assertNotNull(retrievedCustomer, "Der Kunde sollte vor dem Löschen existieren");

            // Kunde löschen
            int rowsAffected = sqlStatement.deleteCustomer(testCustomer.getId());
            dbConnection.getConnection().commit(); // Transaktion abschließen

            assertEquals(1, rowsAffected, "Genau eine Zeile sollte gelöscht worden sein");

            // Prüfen, ob der Kunde nicht mehr existiert
            assertNull(sqlStatement.getCustomer(testCustomer.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Fehler beim Löschen des Kunden: " + e.getMessage());
        }
    }



    @AfterEach
    public void tearDown() throws SQLException {
    	//Löschen der Daten nach jedem Test
    	dbConnection.openConnection(getTestProperties());
    	dbConnection.truncateAllTables();
        // Close the connection after each test
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}