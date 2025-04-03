package org.itp.rest;

import org.itp.server.Server;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Sorgt für eine definierte Reihenfolge der Tests
class ServerTest {

   

    @Test
    void testServerIsRunning() {
    	try {
            Server.startServer("http://localhost:8080/test/ressources/");
        } catch (IOException e) {
            fail("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        Server.stopServer();
        System.out.println("Server wurde gestoppt.");
    }
}
