package org.itp.rest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class TestResourceTest {

    
    public void testGetTestMessage() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/test/ressources/test");

        Response response = target.request().get();
        String responseBody = response.readEntity(String.class);

        assertEquals(200, response.getStatus());
        assertTrue(responseBody.contains("\"message\": \"REST-API läuft erfolgreich!\""));

        client.close();
    }
}
