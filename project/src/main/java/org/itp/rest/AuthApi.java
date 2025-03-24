package org.itp.rest;

import org.itp.dto.UserCredentials;
import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Properties;

@Path("/auth")
public class AuthApi {
    private SQLStatement sqlStatement;

    public AuthApi() {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.openConnection(new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.sqlStatement = new SQLStatement(dbConnection);
    }

    // POST /auth/login - Benutzer authentifizieren
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(UserCredentials credentials) {
        try {
            boolean isAuthenticated = sqlStatement.authenticateUser(credentials.getUsername(), credentials.getPassword());
            if (isAuthenticated) {
                return Response.ok("Login successful").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // POST /auth/create - Benutzer erstellen
    @POST
    @Path("/create")
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(UserCredentials credentials) {
        try {
            // Benutzer erstellen
            boolean userCreated = sqlStatement.createCredentials(credentials.getUsername(), credentials.getPassword());
            if (userCreated) {
                return Response.status(Response.Status.CREATED)
                        .entity("User created successfully")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Username already exists")
                        .build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
