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
    String printDateOfReading();
}