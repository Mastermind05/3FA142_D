package org.itp.project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.itp.dto.Customer;
import org.itp.dto.Reading;
import org.itp.enums.Gender;
import org.itp.enums.KindOfMeter;
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
                rs.getDate("birthDate").toLocalDate(),
                Gender.valueOf(rs.getString("gender"))
        );
    }

    public static List<Customer> getCustomers(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(new Customer(
                    UUIDUtils.bytesAsUUID(rs.getBytes("id")),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getDate("birthDate").toLocalDate(),
                    Gender.valueOf(rs.getString("gender"))
            ));
        }
        return customers;
    }

    public static Reading getReading(ResultSet rs, SQLStatement stmt) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        byte[] customerIdBytes = rs.getBytes("customer_id");
        UUID customerId = customerIdBytes == null ? null : UUIDUtils.bytesAsUUID(customerIdBytes);
        Customer customer = null;

        if (customerId != null) {
            customer = stmt.getCustomer(customerId);
        }

        return new Reading(
                UUIDUtils.bytesAsUUID(rs.getBytes("id")),
                KindOfMeter.valueOf(rs.getString("kindOfMeter")),
                rs.getDate("dateOfReading").toLocalDate(),
                rs.getString("comment"),
                rs.getDouble("meterCount"),
                rs.getString("meterId"),
                rs.getBoolean("substitute"),
                customer
        );
    }

    public static List<Reading> getReadings(ResultSet rs, SQLStatement stmt) throws SQLException {
        List<Reading> readings = new ArrayList<>();
        while (rs.next()) {
            byte[] customerIdBytes = rs.getBytes("customer_id");
            UUID customerId = customerIdBytes == null ? null : UUIDUtils.bytesAsUUID(customerIdBytes);
            Customer customer = null;

            if (customerId != null) {
                customer = stmt.getCustomer(customerId);
            }

            readings.add(new Reading(
                    UUIDUtils.bytesAsUUID(rs.getBytes("id")),
                    KindOfMeter.valueOf(rs.getString("kindOfMeter")),
                    rs.getDate("dateOfReading").toLocalDate(),       
                    rs.getString("comment"),
                    rs.getDouble("meterCount"),
                    rs.getString("meterId"),
                    rs.getBoolean("substitute"),
                    customer
            ));
        }
        return readings;
    }
}
