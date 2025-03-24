package org.itp.project;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.KindOfMeter;
import org.itp.enums.Tables;
import org.itp.utils.UUIDUtils;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.annotation.Nullable;

public class SQLStatement {
	private DBConnection dbConnection = new DBConnection();
	
	public SQLStatement(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

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
	
	public List<Customer> getCustomers() throws SQLException {
        String query = "SELECT * FROM "+Tables.CUSTOMERS+";";
        Statement stmt = dbConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        stmt.close();
        return ObjectMapper.getCustomers(rs);
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
    
    public void createReading(Reading reading) {
    	 String query = "INSERT INTO " + Tables.READINGS + " (id, comment, dateOfReading, kindOfMeter, meterCount, meterId, substitute, customer_id) " +
    	                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    	 try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
    	      stmt.setBytes(1, UUIDUtils.UUIDAsBytes(reading.getId()));
    	        stmt.setString(2, reading.getComment());
    	        stmt.setDate(3, Date.valueOf(reading.getDateOfReading()));
    	        stmt.setString(4, reading.getKindOfMeter().toString());
    	        stmt.setDouble(5, reading.getMeterCount());
    	        stmt.setString(6, reading.getMeterId());
    	        stmt.setBoolean(7, reading.getSubstitute());
    	        stmt.setBytes(8, UUIDUtils.UUIDAsBytes(reading.getCustomer().getId()));

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

    
public void updateReading(Reading reading) {
        String query = "UPDATE " + Tables.READINGS + " SET comment = ?, dateOfReading = ?, kindOfMeter = ?, meterCount = ?, meterId = ?, substitute = ?, customer_id = ? WHERE id = ?";

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, reading.getComment());
            stmt.setDate(2, Date.valueOf(reading.getDateOfReading()));
            stmt.setString(3, reading.getKindOfMeter().toString());
            stmt.setDouble(4, reading.getMeterCount());
            stmt.setString(5, reading.getMeterId());
            stmt.setBoolean(6, reading.getSubstitute());
            stmt.setBytes(7, UUIDUtils.UUIDAsBytes(reading.getCustomer().getId()));
            stmt.setBytes(8, UUIDUtils.UUIDAsBytes(reading.getId()));

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
    
    public List<Reading> getReadings(UUID customerId, LocalDate startDate, LocalDate endDate, KindOfMeter kindOfMeter) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM " + Tables.READINGS + " WHERE customer_id = ?");
        
        if (startDate != null) {
            query.append(" AND dateOfReading >= ?");
        }
        if (endDate != null) {
            query.append(" AND dateOfReading <= ?");
        }
        if (kindOfMeter != null) {
            query.append(" AND kindOfMeter = ?");
        }

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query.toString())) {
            int paramIndex = 1;
            stmt.setBytes(paramIndex++, UUIDUtils.UUIDAsBytes(customerId));

            if (startDate != null) {
                stmt.setDate(paramIndex++, Date.valueOf(startDate));
            }
            if (endDate != null) {
                stmt.setDate(paramIndex++, Date.valueOf(endDate));
            }
            if (kindOfMeter != null) {
                stmt.setString(paramIndex++, kindOfMeter.toString());
            }

            ResultSet rs = stmt.executeQuery();
            return ObjectMapper.getReadings(rs, this);
        }
    }
    
 // Methode zum Erstellen eines Benutzers mit gehashtem Passwort
    public void createCredentials(String username, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12)); // Passwort sicher hashen
        String query = "INSERT INTO " + Tables.AUTHENTIFICATION + " (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Fehler beim Erstellen der Anmeldeinformationen.");
            }
        }
    }

    // Methode zur Authentifizierung des Benutzers mit gehashtem Passwort
    public boolean authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT password FROM " + Tables.AUTHENTIFICATION + " WHERE username = ?";

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return BCrypt.checkpw(password, storedHash); // Vergleicht das eingegebene Passwort mit dem Hash
                }
            }
        }
        return false; // Benutzername nicht gefunden oder falsches Passwort
    }

}
