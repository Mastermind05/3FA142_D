package org.itp.rest;

import java.util.Properties;

import org.itp.project.DBConnection;
import org.itp.project.SQLStatement;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/setupDB") // Wenn du es so aufrufst
public class DBApi {

    private DBConnection dbConnection;
    private SQLStatement sqlStatement;

    public DBApi() {
        try {
            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("credentials.properties"));
            dbConnection = new DBConnection();
            dbConnection.openConnection(props);
            this.sqlStatement = new SQLStatement(dbConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DELETE
    public Response delete() {
        try {
            dbConnection.removeAllTables();
            dbConnection.createAllTables();
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error resetting DB").build();
        }
    }
}
