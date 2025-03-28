package org.itp.project;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

import org.itp.enums.Gender;
import org.itp.enums.KindOfMeter;
import org.itp.enums.Tables;
import org.itp.interfaces.IDatabaseConnection;

public class DBConnection implements IDatabaseConnection {
    private Connection conn;

    @Override
    public DBConnection openConnection(Properties properties) throws SQLException {
        InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("credentials.properties");
        if (input == null) {
            System.out.println("Sorry, unable to find credentials.properties");
        }
        try {
            properties.load(input);
        } catch (IOException e) {
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
        System.out.format("Driver : %s %s.%s\n", meta.getDriverName(), meta.getDriverMajorVersion(), meta.getDriverMinorVersion());
        return this;
    }

    public Connection getConnection() {
        return conn;
    }

    @Override
    public void createAllTables() {
        StringBuilder genderEnums = new StringBuilder();
        String[] genderNames = Arrays.stream(Gender.values()).map(Gender::toString).toArray(String[]::new);
        for (int i = 0; i < genderNames.length; i++) {
            genderEnums.append("'").append(genderNames[i]).append("'");
            if (i < genderNames.length - 1) {
                genderEnums.append(", ");
            }
        }

        String createCustomersTable = "CREATE TABLE IF NOT EXISTS " + Tables.CUSTOMERS + " (" +
                "id BINARY(16) PRIMARY KEY, " +
                "firstName VARCHAR(255) NOT NULL, " +
                "lastName VARCHAR(255) NOT NULL, " +
                "birthDate DATE, " +
                "gender ENUM(" + genderEnums + ")" +
                ");";

        StringBuilder kindOfMeterEnums = new StringBuilder();
        String[] kindOfMeterNames = Arrays.stream(KindOfMeter.values()).map(KindOfMeter::toString).toArray(String[]::new);
        for (int i = 0; i < kindOfMeterNames.length; i++) {
            kindOfMeterEnums.append("'").append(kindOfMeterNames[i]).append("'");
            if (i < kindOfMeterNames.length - 1) {
                kindOfMeterEnums.append(", ");
            }
        }

        String createReadingTable = "CREATE TABLE IF NOT EXISTS " + Tables.READINGS + " (" +
                "id BINARY(16) PRIMARY KEY, " +
                "comment TEXT, " +
                "dateOfReading DATE NOT NULL, " +
                "kindOfMeter ENUM(" + kindOfMeterEnums + ")," +
                "meterCount DOUBLE NOT NULL, " +
                "meterId VARCHAR(255), " +
                "substitute BOOLEAN, " +
                "customer_id BINARY(16), " +
                "CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES " + Tables.CUSTOMERS + "(id) ON DELETE SET NULL" +
                ");";
        String dropAuth = "DROP TABLE " + Tables.AUTHENTIFICATION;
        
        String createAuthTable = "CREATE TABLE IF NOT EXISTS " + Tables.AUTHENTIFICATION + " ("
        	   + "id INT AUTO_INCREMENT PRIMARY KEY,"
        	   +  " username VARCHAR(255) NOT NULL UNIQUE,"
        	   + "password VARCHAR(255) NOT NULL"
        	+")";


        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomersTable);
            stmt.execute(createReadingTable);
            stmt.execute(createAuthTable);
            System.out.println("Die Tabellen wurden erfolgreich erstellt.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Erstellen der Tabellen: " + e.getMessage());
        }
    }

    @Override
    public void truncateAllTables() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("SET foreign_key_checks = 0");
            statement.executeUpdate("TRUNCATE TABLE " + Tables.CUSTOMERS);
            statement.executeUpdate("TRUNCATE TABLE " + Tables.READINGS);
            statement.executeUpdate("TRUNCATE TABLE " + Tables.AUTHENTIFICATION);
            statement.executeUpdate("SET foreign_key_checks = 1");

            System.out.println("Alle Tabellen wurden erfolgreich geleert.");
        } catch (SQLException e) {
            throw new RuntimeException("Error while truncating tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeAllTables() {
        try (Statement statement = conn.createStatement()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    statement.executeUpdate("DROP TABLE " + tableName);
                    System.out.println("Tabelle " + tableName + " wurde erfolgreich gelöscht.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen der Tabellen: " + e.getMessage());
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
            } finally {
                conn = null;
            }
        } else {
            System.out.println("No open database connection to close.");
        }
    }
}
