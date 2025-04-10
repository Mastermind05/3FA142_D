package org.itp.rest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.itp.dto.UserCredentials;
import org.itp.project.DBConnection;
import org.itp.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthApiTest {
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
	
	private Properties getTestProperties() throws IOException {
        // Setze den "user.name" direkt auf "testuser", BEVOR du auf die Properties zugreifst
        System.setProperty("user.name", "testuser");

        Properties properties = new Properties();
        var inputStream = getClass().getClassLoader().getResourceAsStream("credentials.properties");
        properties.load(inputStream);
        return properties;
    }		

	@AfterAll
	public static void stop() {
		Server.stopServer();
	}

	@Test
	public void createandtestAuth() {
		UserCredentials credentials = new UserCredentials("admin", "admin1");
		given()
		.contentType(ContentType.JSON)
		.body(credentials)
		.when()
		.post("/auth/create")
		.then()
		.statusCode(201)
		.body(equalTo("User created successfully"));
		given()
		.contentType(ContentType.JSON)
		.body(credentials)
		.when()
		.post("/auth/login")
		.then()
		.statusCode(200)
		.body(equalTo("Login successful"));
	}
	
	@Test
	public void failedLoginTest() {
		UserCredentials wrongCredentials = new UserCredentials("admin", "wrongpassword");

		given()
			.contentType(ContentType.JSON)
			.body(wrongCredentials)
		.when()
			.post("/auth/login")
		.then()
			.statusCode(401)
			.body(equalTo("Invalid username or password"));
	}
}
