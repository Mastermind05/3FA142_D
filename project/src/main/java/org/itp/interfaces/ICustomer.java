package org.itp.interfaces;

import java.time.LocalDate;

import org.itp.enums.Gender;

public interface ICustomer extends IID{
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setBirthDate(LocalDate birthDate);
    void setGender(Gender gender);

    String getFirstName();
    String getLastName();
    LocalDate getBirthDate();
    Gender getGender();
}