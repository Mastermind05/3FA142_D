package org.itp.rest;

import java.sql.SQLException;
import java.util.Properties;

import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path ("/setupDB")
public class DBApi {
	private DBConnection dbConnection;
	private SQLStatement sqlStatement;
	
    public DBApi() {
        DBConnection dbConnection = new DBConnection();
        try {
            dbConnection.openConnection(new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.sqlStatement = new SQLStatement(dbConnection);
    }
	
	@DELETE
	public Response delete() {
		dbConnection = new DBConnection();
		dbConnection.removeAllTables();
		dbConnection.createAllTables();
		return Response.ok().build();
	}
}
