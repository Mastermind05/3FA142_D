package org.itp.project;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.Gender;
import org.itp.utils.UUIDUtils;

public class ObjectMapper {
	public static Customer getCustomer(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        return new Customer(
                UUIDUtils.bytesAsUUID(rs.getBytes("id")),
                rs.getString("firstName"),
                rs.getString("lastName"),
                Gender.valueOf(rs.getString("gender")),
                rs.getDate("birthDate").toLocalDate()
        );
    }
	/*public static Reading getReading(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			return null;
		} 
		
				
	}*/
}
