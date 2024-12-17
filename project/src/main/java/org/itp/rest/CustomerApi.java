package org.itp.rest;

import org.itp.dto.Customer;
import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

@Path("/customers")
public class CustomerApi {
    private SQLStatement sqlStatement;
    public CustomerApi() {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.openConnection(new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.sqlStatement = new SQLStatement(dbConnection);
    }
    // POST /customers - Create a new customer
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createCustomer(Customer customer) {
        try {
            sqlStatement.createCustomer(customer);
            return Response.status(Response.Status.CREATED).entity(customer).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET /customers/{uuid} - Get a specific customer by ID
    @GET
    @Path("/{uuid}")
    @Produces("application/json")
    public Response getCustomer(@PathParam("uuid") String uuid) {
        try {
            Customer customer = sqlStatement.getCustomer(UUID.fromString(uuid));
            if (customer != null) {
                return Response.ok(customer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // PUT /customers - Update a customer
    @PUT
    @Consumes("application/json")
    @Produces("text/plain")
    public Response updateCustomer(Customer customer) {
        try {
            int rowsAffected = sqlStatement.updateCustomer(customer);
            if (rowsAffected > 0) {
                return Response.ok("Customer updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // DELETE /customers/{uuid} - Delete a customer by ID
    @DELETE
    @Path("/{uuid}")
    @Produces("text/plain")
    public Response deleteCustomer(@PathParam("uuid") String uuid) {
        try {
            int rowsAffected = sqlStatement.deleteCustomer(UUID.fromString(uuid));
            if (rowsAffected > 0) {
                return Response.ok("Customer deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
