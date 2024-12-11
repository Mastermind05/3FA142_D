package org.itp.project;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.Tables;
import org.itp.utils.UUIDUtils;

import jakarta.annotation.Nullable;

public class SQLStatement {
	private DBConnection dbConnection;

	//Customer Part
	
	// ToDo Select by Name
	
	public Customer getCustomer(UUID id) throws SQLException {
        String query = "SELECT * FROM "+Tables.CUSTOMERS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(id));
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return ObjectMapper.getCustomer(rs);
    }
	

    public void createCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO "+Tables.CUSTOMERS+" (id, firstName, lastName, birthDate, gender) VALUES (?,?,?,?,?);";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(customer.getId()));
        stmt.setString(2, customer.getFirstName());
        stmt.setString(3, customer.getLastName());
        stmt.setDate(4, Date.valueOf(customer.getBirthDate()));
        stmt.setString(5, customer.getGender().toString());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Das Erstellen des Kunden ist fehlgeschlagen");
        }

        stmt.close();
    }

    public int updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE "+Tables.CUSTOMERS+" SET firstName = ?, lastName = ?, birthDate = ?, gender = ? WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
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
        String query = "DELETE FROM "+Tables.CUSTOMERS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(customerId));

        int rowsAffected = stmt.executeUpdate();
        stmt.close();

        return rowsAffected;
    }
    
    public void createReading(UUID id, String comment, LocalDate dateOfReading, String kindOfMeter, double meterCount, String meterId, boolean substitute, UUID customerId) {
    	 String query = "INSERT INTO " + Tables.READINGS + " (id, comment, dateOfReading, kindOfMeter, meterCount, meterId, substitute, customer_id) " +
    	                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    	 try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
    	      stmt.setBytes(1, UUIDUtils.UUIDAsBytes(id));
    	        stmt.setString(2, comment);
    	        stmt.setDate(3, java.sql.Date.valueOf(dateOfReading));
    	        stmt.setString(4, kindOfMeter);
    	        stmt.setDouble(5, meterCount);
    	        stmt.setString(6, meterId);
    	        stmt.setBoolean(7, substitute);
    	        stmt.setBytes(8, UUIDUtils.UUIDAsBytes(customerId));

    	        stmt.executeUpdate();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}

    @Nullable
    public Reading getReading(UUID id) throws SQLException {
        String query = "SELECT * FROM "+Tables.READINGS+" WHERE id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(id));
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return ObjectMapper.getReading(rs, this);
    }

    public List<Reading> getReadingsByCustomerId(UUID customerId) throws SQLException {
        String query = "SELECT * FROM "+Tables.READINGS+" WHERE customer_id = ?;";
        PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query);
        stmt.setBytes(1, UUIDUtils.UUIDAsBytes(customerId));
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        return ObjectMapper.getReadings(rs, this);
    }

    
public void updateReading(UUID id, String comment, LocalDate dateOfReading, String kindOfMeter, double meterCount, String meterId, boolean substitute, UUID customerId) {
        String query = "UPDATE " + Tables.READINGS + " SET comment = ?, dateOfReading = ?, kindOfMeter = ?, meterCount = ?, meterId = ?, substitute = ?, customer_id = ? WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, comment);
            stmt.setDate(2, java.sql.Date.valueOf(dateOfReading));
            stmt.setString(3, kindOfMeter);
            stmt.setDouble(4, meterCount);
            stmt.setString(5, meterId);
            stmt.setBoolean(6, substitute);
            stmt.setBytes(7, UUIDUtils.UUIDAsBytes(customerId));
            stmt.setBytes(8, UUIDUtils.UUIDAsBytes(id));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteReading(UUID readingid) {
        String query = "DELETE FROM " + Tables.READINGS + " WHERE id = ?";
        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
            stmt.setBytes(1, UUIDUtils.UUIDAsBytes(readingid));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
