package org.itp.rest;

import java.sql.SQLException;
import java.util.Properties;

import org.itp.project.DBConnection;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path ("/setupDB")
public class DBApi {
	private DBConnection dbConnection;
	
	@DELETE
	public Response delete() {
		dbConnection = new DBConnection();
		dbConnection.removeAllTables();
		dbConnection.createAllTables();
		return Response.ok().build();
	}
}
