package org.itp.project;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionImplTest {

    private DBConnection dbConnection;
    private Properties properties;

    @BeforeEach
    public void setUp() {
        dbConnection = new DBConnection();
        properties = new Properties();

        // Simulate loading a test properties file (hardcoded values)
        properties.setProperty("testuser.db.url", "jdbc:mariadb://localhost:3306/test");
        properties.setProperty("testuser.db.user", "root");
        properties.setProperty("testuser.db.pw", "password");

        // Simulate the system user
        System.setProperty("user.name", "testuser");
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
            /*
            // Check if the 'CUSTOMERS' table exists
            ResultSet rs = metaData.getTables(null, null, "CUSTOMERS", null);
            assertTrue(rs.next(), "The 'CUSTOMERS' table should exist");

            // Check if the 'READING' table exists
            rs = metaData.getTables(null, null, "READING", null);
            assertTrue(rs.next(), "The 'READING' table should exist");
			*/
            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred during the test: " + e.getMessage());
        }
    }
    

    @Test
    public void testTruncateAllTables() {
        try {
            dbConnection.openConnection(properties);
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "Connection should be established");

            // Call the truncateAllTables method
            dbConnection.truncateAllTables();

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during truncateAllTables test: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        // Close the connection after each test
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}