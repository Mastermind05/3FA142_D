package org.itp.project;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.Gender;
import org.itp.enums.KindOfMeter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SQLStatementTest {

    private DBConnection dbConnection;
    @SuppressWarnings("unused")
	private Properties properties;
    private SQLStatement sqlStatement;
    private Customer testCustomer;
    private Reading testReading;
    UUID customerUUID = UUID.fromString("f8a1a58c-91d9-4f39-b432-8e5a58f8c73d");
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
        		customerUUID,
                "Max",
                "Mustermann",
                LocalDate.of(1990, 1, 1),
                Gender.M
        );
     // Erstelle ein Test-Reading
        testReading = new Reading(
                UUID.randomUUID(),
                KindOfMeter.STROM,
                LocalDate.now(),
                "Erstablesung",
                1234.56,
                "Meter-001",
                false,
                testCustomer
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
    public void testCreateReading() {
        try {
            // Kunde erstellen und committen
            sqlStatement.createCustomer(testCustomer);
            dbConnection.getConnection().commit(); // Sicherstellen, dass der Kunde gespeichert wird

            // Reading erstellen
            sqlStatement.createReading(testReading);
            dbConnection.getConnection().commit(); // Sicherstellen, dass das Reading gespeichert wird

            // Reading abrufen und prüfen
            Reading retrievedReading = sqlStatement.getReading(testReading.getId());
            assertNotNull(retrievedReading, "Das Reading sollte erfolgreich erstellt worden sein");
            assertEquals(testReading.getComment(), retrievedReading.getComment(), "Der Kommentar sollte übereinstimmen");

        } catch (SQLException e) {
            fail("Fehler beim Erstellen des Readings: " + e.getMessage());
        }
    }


    @Test
    public void testGetReadingsByCustomerId() {
        try {
        	sqlStatement.createCustomer(testCustomer);
            dbConnection.getConnection().commit(); // Sicherstellen, dass der Kunde gespeichert wird
            sqlStatement.createReading(testReading);

            List<Reading> readings = sqlStatement.getReadingsByCustomerId(testCustomer.getId());
            assertFalse(readings.isEmpty(), "Es sollte mindestens ein Reading für den Kunden geben");
            assertEquals(testReading.getId(), readings.get(0).getId(), "Die Reading-ID sollte übereinstimmen");
        } catch (SQLException e) {
            fail("Fehler beim Abrufen der Readings: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateReading() {
        try {
        	sqlStatement.createCustomer(testCustomer);
            dbConnection.getConnection().commit(); // Sicherstellen, dass der Kunde gespeichert wird
            sqlStatement.createReading(testReading);

            // Aktualisiere den Kommentar des Readings
            testReading.setComment("Aktualisierter Kommentar");
            sqlStatement.updateReading(testReading);

            Reading updatedReading = sqlStatement.getReading(testReading.getId());
            assertEquals("Aktualisierter Kommentar", updatedReading.getComment(), "Der Kommentar sollte aktualisiert worden sein");
        } catch (SQLException e) {
            fail("Fehler beim Aktualisieren des Readings: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteReading() {
        try {
        	sqlStatement.createCustomer(testCustomer);
            dbConnection.getConnection().commit(); // Sicherstellen, dass der Kunde gespeichert wird
            sqlStatement.createReading(testReading);

            // Lösche das Reading
            sqlStatement.deleteReading(testReading.getId());

            Reading deletedReading = sqlStatement.getReading(testReading.getId());
            assertNull(deletedReading, "Das Reading sollte nach dem Löschen nicht mehr existieren");
        } catch (SQLException e) {
            fail("Fehler beim Löschen des Readings: " + e.getMessage());
        }
    }
        @Test
        public void testGetReadingsWithAllParameters() {
            try {
            	sqlStatement.createCustomer(testCustomer);
                // Beispiel-Readings erstellen
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.STROM,
                    LocalDate.of(2023, 1, 1),
                    "Erstablesung",
                    1234.56,
                    "Meter-001",
                    false,
                    testCustomer
                ));
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.WASSER,
                    LocalDate.of(2023, 5, 1),
                    "Zwischenablesung",
                    1500.75,
                    "Meter-002",
                    false,
                    testCustomer
                ));
                dbConnection.getConnection().commit();

                // Test mit allen Parametern
                LocalDate startDate = LocalDate.of(2023, 1, 1);
                LocalDate endDate = LocalDate.of(2023, 12, 31);
                KindOfMeter kindOfMeter = KindOfMeter.STROM;

                List<Reading> readings = sqlStatement.getReadings(testCustomer.getId(), startDate, endDate, kindOfMeter);
                assertNotNull(readings);
                assertEquals(1, readings.size(), "Es sollte nur ein Reading für diesen Zeitraum und Typ vorhanden sein");
                assertEquals(KindOfMeter.STROM, readings.get(0).getKindOfMeter(), "Das Reading sollte vom Typ STROM sein");
            } catch (SQLException e) {
                fail("Fehler beim Abrufen der Readings: " + e.getMessage());
            }
        }

        @Test
        public void testGetReadingsWithStartDateOnly() {
            try {
            	sqlStatement.createCustomer(testCustomer);
                // Beispiel-Readings erstellen
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.STROM,
                    LocalDate.of(2023, 1, 1),
                    "Erstablesung",
                    1234.56,
                    "Meter-001",
                    false,
                    testCustomer
                ));
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.WASSER,
                    LocalDate.of(2023, 5, 1),
                    "Zwischenablesung",
                    1500.75,
                    "Meter-002",
                    false,
                    testCustomer
                ));
                dbConnection.getConnection().commit();

                // Test mit nur Startdatum
                LocalDate startDate = LocalDate.of(2023, 1, 1);
                List<Reading> readings = sqlStatement.getReadings(testCustomer.getId(), startDate, null, null);
                assertNotNull(readings);
                assertTrue(readings.size() >= 2, "Es sollten mindestens zwei Readings nach dem Startdatum vorhanden sein");
            } catch (SQLException e) {
                fail("Fehler beim Abrufen der Readings: " + e.getMessage());
            }
        }

        @Test
        public void testGetReadingsWithEndDateOnly() {
            try {
            	sqlStatement.createCustomer(testCustomer);
                // Beispiel-Readings erstellen
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.STROM,
                    LocalDate.of(2023, 1, 1),
                    "Erstablesung",
                    1234.56,
                    "Meter-001",
                    false,
                    testCustomer
                ));
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.WASSER,
                    LocalDate.of(2023, 5, 1),
                    "Zwischenablesung",
                    1500.75,
                    "Meter-002",
                    false,
                    testCustomer
                ));
                dbConnection.getConnection().commit();

                // Test mit nur Enddatum
                LocalDate endDate = LocalDate.of(2023, 3, 1);
                List<Reading> readings = sqlStatement.getReadings(testCustomer.getId(), null, endDate, null);
                assertNotNull(readings);
                assertTrue(readings.size() >= 1, "Es sollten mindestens Readings vor dem Enddatum vorhanden sein");
            } catch (SQLException e) {
                fail("Fehler beim Abrufen der Readings: " + e.getMessage());
            }
        }

        @Test
        public void testGetReadingsWithKindOfMeterOnly() {
            try {
            	sqlStatement.createCustomer(testCustomer);
                // Beispiel-Readings erstellen
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.STROM,
                    LocalDate.of(2023, 1, 1),
                    "Erstablesung",
                    1234.56,
                    "Meter-001",
                    false,
                    testCustomer
                ));
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.WASSER,
                    LocalDate.of(2023, 5, 1),
                    "Zwischenablesung",
                    1500.75,
                    "Meter-002",
                    false,
                    testCustomer
                ));
                dbConnection.getConnection().commit();

                // Test mit nur KindOfMeter
                List<Reading> readings = sqlStatement.getReadings(testCustomer.getId(), null, null, KindOfMeter.STROM);
                assertNotNull(readings);
                assertEquals(1, readings.size(), "Es sollte nur ein Reading vom Typ STROM vorhanden sein");
                assertEquals(KindOfMeter.STROM, readings.get(0).getKindOfMeter(), "Das Reading sollte vom Typ STROM sein");
            } catch (SQLException e) {
                fail("Fehler beim Abrufen der Readings: " + e.getMessage());
            } 
            }

        @Test
        public void testGetReadingsWithNoFilters() {
            try {
            	sqlStatement.createCustomer(testCustomer);
                // Beispiel-Readings erstellen
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.STROM,
                    LocalDate.of(2023, 1, 1),
                    "Erstablesung",
                    1234.56,
                    "Meter-001",
                    false,
                    testCustomer
                ));
                sqlStatement.createReading(new Reading(
                    UUID.randomUUID(),
                    KindOfMeter.WASSER,
                    LocalDate.of(2023, 5, 1),
                    "Zwischenablesung",
                    1500.75,
                    "Meter-002",
                    false,
                    testCustomer
                ));
                dbConnection.getConnection().commit();

                // Test mit keinem Filter
                List<Reading> readings = sqlStatement.getReadings(testCustomer.getId(), null, null, null);
                assertNotNull(readings);
                assertTrue(readings.size() >= 2, "Es sollten mindestens zwei Readings vorhanden sein");
            } catch (SQLException e) {
                fail("Fehler beim Abrufen der Readings: " + e.getMessage());
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