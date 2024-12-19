package org.itp.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Properties;

import org.itp.dto.Reading;
import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;
import org.itp.utils.UUIDUtils;

@Path("/readings")
public class ReadingsApi {

    private SQLStatement sqlStatement;

    public ReadingsApi() {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.openConnection(new Properties()); // Hier sicherstellen, dass die Properties korrekt geladen werden
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.sqlStatement = new SQLStatement(dbConnection);
    }

    // GET Reading by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingById(@PathParam("id") String id) {
        try {
            Reading reading = sqlStatement.getReading(UUIDUtils.fromString(id));
            if (reading == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Reading not found").build();
            }
            return Response.ok(reading).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    // CREATE Reading
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReading(Reading reading) {
        try {
            sqlStatement.createReading(reading);
            return Response.status(Response.Status.CREATED).entity("Reading created successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // UPDATE Reading
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReading(@PathParam("id") String id, Reading reading) {
        try {
            reading.setId(UUIDUtils.fromString(id)); // Sicherstellen, dass die ID gesetzt wird
            sqlStatement.updateReading(reading);
            return Response.ok("Reading updated successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    // DELETE Reading
    @DELETE
    @Path("/{id}")
    public Response deleteReading(@PathParam("id") String id) {
        try {
            sqlStatement.deleteReading(UUIDUtils.fromString(id));
            return Response.ok("Reading deleted successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
