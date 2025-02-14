package org.itp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerDto {
	private Customer customer;
	@JsonCreator
    public CustomerDto(@JsonProperty("customer") Customer customer) {
        this.customer = customer;
    }
	public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
