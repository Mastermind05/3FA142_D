package org.itp.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.Gender;
import org.itp.enums.KindOfMeter;
import org.itp.project.DBConnection;
import org.itp.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ReadingsApiTest {
    private static DBConnection dbConnection;
    private static Properties properties;
    @BeforeAll
	public static void start() throws IOException {
    	Server.startServer("http://localhost:8080/test/ressources/");
    	RestAssured.baseURI = "http://localhost:8080/test/ressources";

    }
    
    
    @BeforeEach
    public void setup() throws IOException {
    	 dbConnection = new DBConnection();
    	    properties = new Properties();
    	    try {
    	        dbConnection.openConnection(properties);
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    }
    
    @AfterAll
    public static void tearDown() throws SQLException {
		dbConnection.createAllTables();
		dbConnection.truncateAllTables();
        // Close the connection after each test
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
        System.out.println("Reading abgeschlossen");
    }
    
    @AfterAll
    public static void stop() {
    	Server.stopServer();
    }
    

    @Test
    @DisplayName(value = "testGetReadingById_NotFound")
    public void testGetReadingById_NotFound() {
        String fakeId = UUID.randomUUID().toString();
        given()
                .when()
                .get("/readings/" + fakeId)
                .then()
                .statusCode(404)
                .body(equalTo("Reading not found"));
    }

    @Test
    @DisplayName("testCreateReading")
    public void testCreateReading() {
        // 1. Customer vorbereiten
        Customer customer = new Customer("John", "Doe", Gender.M, LocalDate.of(1990, 5, 15));

        // 2. Customer anlegen und mit ID zurückbekommen
        Customer createdCustomer = given()
                .contentType(ContentType.JSON)
                .body(customer)
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .extract()
                .as(Customer.class);

        // 3. Reading mit dem erstellten Customer anlegen
        Reading reading = new Reading(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                KindOfMeter.STROM,
                LocalDate.of(2024, 6, 15),
                "Zählerstand wurde abgelesen",
                12345.67,
                "MTR-001",
                false,
                createdCustomer // <-- Der Customer mit gültiger ID
        );

        // 4. Reading speichern
        given()
                .contentType(ContentType.JSON)
                .body(reading)
                .when()
                .post("/readings")
                .then()
                .statusCode(201)
                .body(equalTo("Reading created successfully"));
    }

    
/*    @Test
    public void testGetReadingById() {
        UUID Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        given()
                .when()
                .get("/readings/" + Id)
                .then()
                .statusCode(200)
                .body(equalTo("Reading not found"));
    }*/

    @Test
    @DisplayName(value = "testUpdateReading")
    public void testUpdateReading() {
        Customer customer = new Customer("Jane", "Doe", Gender.W, LocalDate.of(1985, 7, 20));
        UUID readingId = UUID.randomUUID();
        Reading updatedReading = new Reading(
                readingId,
                KindOfMeter.HEIZUNG,
                LocalDate.now(),
                "Updated comment",
                200.00,
                "Meter456",
                true,
                customer
        );

        given()
                .contentType(ContentType.JSON)
                .body(updatedReading)
                .when()
                .put("/readings/" + readingId)
                .then()
                .statusCode(200)
                .body(equalTo("Reading updated successfully"));
    } 

    @Test
    @DisplayName(value = "testDeleteReading")
    public void testDeleteReading() {
        UUID readingId = UUID.randomUUID();

        given()
                .when()
                .delete("/readings/" + readingId)
                .then()
                .statusCode(200)
                .body(equalTo("Reading deleted successfully"));
    }

   /* @Test
    public void testGetReadings() {
        given()
                .queryParam("customer", UUID.randomUUID().toString())
                .queryParam("start", "2024-01-01")
                .queryParam("end", "2024-12-31")
                .queryParam("kindOfMeter", "WATER")
                .when()
                .get("/readings")
                .then()
                .statusCode(200)
                .body(anyOf(equalTo("[]"), not(empty())));
    } */
}
