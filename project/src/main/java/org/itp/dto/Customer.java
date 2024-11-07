package org.itp.dto;

import java.time.LocalDate;
import java.util.UUID;

import org.itp.enums.Gender;
import org.itp.interfaces.ICustomer;

public class Customer implements ICustomer{

	private UUID id;
	private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    
    public Customer(UUID id, String firstName, String lastName, Gender gender, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }
    public Customer(String firstName, String lastName, Gender gender, LocalDate birthDate) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
    }
	
	@Override
	public void setFirstName(String firstName) {
		// TODO Auto-generated method stub
		this.firstName = firstName;
	}

	@Override
	public void setLastName(String lastName) {
		// TODO Auto-generated method stub
		this.lastName = lastName;
	}

	@Override
	public void setBirthDate(LocalDate birthDate) {
		// TODO Auto-generated method stub
		this.birthDate = birthDate;
	}

	@Override
	public void setGender(Gender gender) {
		// TODO Auto-generated method stub
		this.gender = gender;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return lastName;
	}

	@Override
	public LocalDate getBirthDate() {
		// TODO Auto-generated method stub
		return birthDate;
	}

	@Override
	public Gender getGender() {
		// TODO Auto-generated method stub
		return gender;
	}

	@Override
	public UUID getId() {
		return id;
	}
	@Override
	public void setId(UUID id) {
		this.id = id;
		
	}

}
