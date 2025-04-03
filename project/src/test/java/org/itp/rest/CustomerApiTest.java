package org.itp.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.enums.Gender;
import org.itp.project.DBConnection;
import org.itp.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

public class CustomerApiTest {

    private DBConnection dbConnection;
    private Properties properties;
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
    	        properties.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));
    	        System.out.println(properties);
    	        dbConnection.openConnection(properties);
    	    } catch (SQLException | IOException e) {
    	        e.printStackTrace();
    	    }
    }
    
    @AfterEach
    public void tearDown() throws SQLException {
    	//Löschen der Daten nach jedem Test
    	try {
	        properties.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));
	        System.out.println(properties);
	        dbConnection.openConnection(properties);
	    	dbConnection.createAllTables();
	    	dbConnection.truncateAllTables();
	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	    }
        // Close the connection after each test
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
    
    @AfterAll
    public static void stop() {
    	Server.stopServer();
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer("John", "Doe", Gender.M, LocalDate.parse("1992-02-02"));

        given()
            .contentType(ContentType.JSON)
            .body(customer)
        .when()
            .post("customers")
        .then()
            .statusCode(201)
            .assertThat();
    }

    @Test
    public void testGetAllCustomers() {
    	RestAssured.get("/customers")
        .then().assertThat()
        .statusCode(200)
      //  .body(matchesJsonSchemaInClasspath("JSON_Schema_Customers.json"))
        ;
    }

    @Test
    public void testGetCustomerById() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId,"Jane", "Doe", LocalDate.parse("1992-02-02"), Gender.W);

        given()
            .contentType(ContentType.JSON)
            .body(customer)
            .post("customers");

        when()
            .get("customers/" + customerId)
        .then()
            .statusCode(200)
            .assertThat()
  //         .body(matchesJsonSchemaInClasspath("JSON_Schema_Customer.json"))
            ;
    }

    @Test
    public void testUpdateCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId,"Old", "Name", LocalDate.of(1985, 5, 5), Gender.M);

        given()
            .contentType(ContentType.JSON)
            .body(customer)
            .post("/customers");

        customer = new Customer(customerId, "Updated", "Name", LocalDate.of(1985, 5, 5), Gender.M);

        given()
            .contentType(ContentType.JSON)
            .body(customer)
        .when()
            .put("customers")
        .then()
            .statusCode(200)
            .body(equalTo("Customer updated successfully"));
    }

    @Test
    public void testDeleteCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId,"To Be", "Deleted", LocalDate.of(2000, 10, 10), Gender.U);

        given()
            .contentType(ContentType.JSON)
            .body(customer)
            .post("customers");

        when()
            .delete("customers/" + customerId)
        .then()
            .statusCode(200)
            .body(equalTo("Customer deleted successfully"));
    } 
}
