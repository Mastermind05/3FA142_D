package org.itp.rest;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

import org.itp.dto.Customer;
import org.itp.enums.Gender;
import org.itp.project.DBConnection;
import org.itp.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class DBApiTest {
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
		try {
			dbConnection.openConnection(getTestProperties());
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private Properties getTestProperties() throws IOException {
        // Setze den "user.name" direkt auf "testuser", BEVOR du auf die Properties zugreifst
        System.setProperty("user.name", "testuser");

        Properties properties = new Properties();
        var inputStream = getClass().getClassLoader().getResourceAsStream("credentials.properties");
        properties.load(inputStream);
        return properties;
    }	

	@AfterAll
	public static void tearDown() throws SQLException {
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
	@DisplayName("testDeleteAndRecreateTables")
	public void testDeleteAndRecreateTables() {

	    Customer customer = new Customer("Temp", "User", Gender.D, LocalDate.of(2000, 1, 1));
	    given()
	        .contentType(ContentType.JSON)
	        .body(customer)
	        .when()
	        .post("/customers")
	        .then()
	        .statusCode(201);

	    given()
	        .when()
	        .delete("/setupDB") 
	        .then()
	        .statusCode(200);

	    given()
	        .when()
	        .get("/customers")
	        .then()
	        .statusCode(200)
	        .body("size()", equalTo(0)); // Erwartung: 0 Einträge nach Reset
	}

}
