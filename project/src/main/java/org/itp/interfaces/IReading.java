package org.itp.interfaces;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.itp.enums.KindOfMeter;

public interface IReading {
    void setComment(String comment);
    void setCustomer(ICustomer customer);
    void setDateOfReading(LocalDate dateOfReading);
    void setKindOfMeter(KindOfMeter kindOfMeter);
    void setMeterCount(double meterCount);
    void setMeterId(String meterId);
    void setSubstitute(boolean substitute);

    String getComment();
    ICustomer getCustomer();
    LocalDate getDateOfReading();
    KindOfMeter getKindOfMeter();
    double getMeterCount();
    String getMeterId();
    boolean getSubstitute();

    default String printDateOfReading() {
        // Get the date of reading
        LocalDate date = getDateOfReading();
        // Format the date in a readable format (e.g. "dd-MM-yyyy")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date != null ? date.format(formatter) : "No date available";
    }
}