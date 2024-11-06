package org.itp.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.enums.Tables;
import org.itp.utils.UUIDUtils;

public class SQLStatement {
	private DBConnection dbConnection;
	//Select Costumer mit der UUID
	public Customer getCustomer(UUID id) throws SQLException {
        String query = "SELECT * FROM "+Tables.CUSTOMERS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(id));
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return ObjectMapper.getCustomer(rs);
    }
}
