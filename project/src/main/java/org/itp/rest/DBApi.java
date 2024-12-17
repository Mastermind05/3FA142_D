package org.itp.rest;

import java.sql.SQLException;
import java.util.Properties;

import org.itp.project.DBConnection;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path ("/setupDB")
public class DBApi {
	private final DBConnection dbConnection;
	public DBApi() {
        dbConnection = new DBConnection();
        try {
            dbConnection.openConnection(new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	@DELETE
	public Response delete() {
		dbConnection.removeAllTables();
		dbConnection.createAllTables();
		return Response.ok().build();
	}
}
