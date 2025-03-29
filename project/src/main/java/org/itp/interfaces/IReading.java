package org.itp.interfaces;

import java.time.LocalDate;

import org.itp.dto.Customer;
import org.itp.enums.KindOfMeter;

public interface IReading extends IID{
    void setComment(String comment);
    void setCustomer(Customer customer);
    void setDateOfReading(LocalDate dateOfReading);
    void setKindOfMeter(KindOfMeter kindOfMeter);
    void setMeterCount(double meterCount);
    void setMeterId(String meterId);
    void setSubstitute(boolean substitute);

    String getComment();
    Customer getCustomer();
    LocalDate getDateOfReading();
    KindOfMeter getKindOfMeter();
    double getMeterCount();
    String getMeterId();
    boolean getSubstitute();
    String printDateOfReading();
}