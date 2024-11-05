package org.itp.project;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.itp.interfaces.IDatabaseConnection;

public class DBConnection implements IDatabaseConnection{
	private Connection conn;
	@Override
	public DBConnection openConnection(Properties properties) throws SQLException {
		
	    // Der Pfad zur credentials.properties Datei
		// Properties Datei einlesen
		InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("credentials.properties");
        
        
        if (input == null) {
            System.out.println("Sorry, unable to find credentials.properties");
        }
        
        
        try {
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		      String systemUser = System.getProperty("user.name");
		      String url = properties.getProperty(systemUser + ".db.url");
		      String user = properties.getProperty(systemUser + ".db.user");
		      String password = properties.getProperty(systemUser + ".db.pw");
		      
		      if (conn != null) {
		    	  return this;
		      }
		      conn = DriverManager.getConnection(url, user, password);
		      final DatabaseMetaData meta = conn.getMetaData();
		      System.out.format("Driver : %s %s.%s\n", meta.getDriverName(),
		               meta.getDriverMajorVersion(), meta.getDriverMinorVersion());
		      return this;
		      
		   }
	
	public Connection getConnection() {
        return conn;
    }
	
    
	

	@Override
	public void createAllTables() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void truncateAllTables() {
		Statement statement = null;
		try {
            statement = conn.createStatement();
            
            // SQL TRUNCATE-Befehle für die Tabellen Customer und Reading
            statement.executeUpdate("TRUNCATE TABLE Customer");
            statement.executeUpdate("TRUNCATE TABLE Reading");

            System.out.println("Die Tabellen Customer und Reading wurden erfolgreich geleert.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void removeAllTables() {
	    Statement statement = null;
	    try {
	        if (conn == null || conn.isClosed()) {
	            System.out.println("Connection is not open. Cannot remove tables.");
	            return;
	        }
	        
	        DatabaseMetaData metaData = conn.getMetaData();
	        statement = conn.createStatement();

	        // Alle Tabellen in der Datenbank abrufen
	        try (ResultSet tables = metaData.getTables(null, null, "%", new String[] {"TABLE"})) {
	            while (tables.next()) {
	                String tableName = tables.getString("TABLE_NAME");
	                
	                // SQL DROP TABLE-Befehl für jede Tabelle
	                statement.executeUpdate("DROP TABLE " + tableName);
	                System.out.println("Tabelle " + tableName + " wurde erfolgreich gelöscht.");
	            }
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Löschen der Tabellen: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        try {
	            if (statement != null) statement.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
		
	}

	@Override
	public void closeConnection() {
		if (conn != null) {
	        try {
	            conn.close();
	            System.out.println("Database connection closed successfully.");
	        } catch (SQLException e) {
	            System.err.println("Error closing the database connection: " + e.getMessage());
	            e.printStackTrace();
	        } finally {
	            conn = null; // Setzt die Verbindung auf null, um doppelte Schließungen zu vermeiden
	        }
	    } else {
	        System.out.println("No open database connection to close.");
	    }
		
	}


}
