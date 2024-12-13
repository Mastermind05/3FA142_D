package org.itp.rest;

import org.itp.project.DBConnection;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path ("/setupDB")
public class DBApi {
	private final DBConnection dbConnection = new DBConnection();
	@DELETE
	public Response delete() {
		dbConnection.removeAllTables();
		dbConnection.createAllTables();
		return Response.ok().build();
	}
}
