package org.itp.project;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.enums.Tables;
import org.itp.utils.UUIDUtils;

public class SQLStatement {
	private DBConnection dbConnection;

	//Customer Part
	
	public Customer getCustomer(UUID id) throws SQLException {
        String query = "SELECT * FROM "+Tables.CUSTOMERS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(id));
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return ObjectMapper.getCustomer(rs);
    }
	

    public void createCustomer(Customer customer) throws SQLException {
        String mutation = "INSERT INTO "+Tables.CUSTOMERS+" (id, firstName, lastName, birthDate, gender) VALUES (?,?,?,?,?);";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(mutation);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(customer.getId()));
        stmt.setString(2, customer.getFirstName());
        stmt.setString(3, customer.getLastName());
        stmt.setDate(4, Date.valueOf(customer.getBirthDate()));
        stmt.setString(5, customer.getGender().toString());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Customer creation failed");
        }

        stmt.close();
    }

    public int updateCustomer(Customer customer) throws SQLException {
        String mutation = "UPDATE "+Tables.CUSTOMERS+" SET firstName = ?, lastName = ?, birthDate = ?, gender = ? WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(mutation);
        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        stmt.setDate(3, Date.valueOf(customer.getBirthDate()));
        stmt.setString(4, customer.getGender().toString());
        stmt.setBytes(5, UUIDUtils.UUIDAsBytes(customer.getId()));

        int rowsAffected = stmt.executeUpdate();
        stmt.close();

        return rowsAffected;
    }

    public int deleteCustomer(UUID customerId) throws SQLException {
        String mutation = "DELETE FROM "+Tables.CUSTOMERS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(mutation);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(customerId));

        int rowsAffected = stmt.executeUpdate();
        stmt.close();

        return rowsAffected;
    }
    //Reading Part
}
