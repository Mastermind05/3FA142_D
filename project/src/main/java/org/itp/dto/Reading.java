package org.itp.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.itp.enums.KindOfMeter;
import org.itp.interfaces.ICustomer;
import org.itp.interfaces.IReading;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Reading implements IReading{
	
	private UUID id;
	private String comment;
    private Customer customer;
    private LocalDate dateOfReading;
    private KindOfMeter kindOfMeter;
    private double meterCount;
    private String meterId;
    private boolean substitute;
    
    @JsonCreator
    public Reading(
        @JsonProperty("id") UUID id,
        @JsonProperty("kindOfMeter") KindOfMeter kindOfMeter,
        @JsonProperty("dateOfReading") LocalDate dateOfReading,
        @JsonProperty("comment") String comment,
        @JsonProperty("meterCount") double meterCount,
        @JsonProperty("meterId") String meterId,
        @JsonProperty("substitute") Boolean substitute,
        @JsonProperty("customer") Customer customer
    ) {
        this.id = Objects.requireNonNullElseGet(id, UUID::randomUUID);
        this.kindOfMeter = kindOfMeter;
        this.dateOfReading = dateOfReading;
        this.customer = customer;
        this.comment = comment;
        this.meterCount = meterCount;
        this.meterId = meterId;
        this.substitute = substitute;
    }
    
    
    @Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public void setDateOfReading(LocalDate dateOfReading) {
		this.dateOfReading = dateOfReading;
	}

	@Override
	public void setKindOfMeter(KindOfMeter kindOfMeter) {
		this.kindOfMeter = kindOfMeter;
	}

	@Override
	public void setMeterCount(double meterCount) {
		this.meterCount = meterCount;
	}

	@Override
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	@Override
	public void setSubstitute(boolean substitute) {
		this.substitute = substitute;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public LocalDate getDateOfReading() {
		return dateOfReading;
	}

	@Override
	public KindOfMeter getKindOfMeter() {
		return kindOfMeter;
	}

	@Override
	public double getMeterCount() {
		return meterCount;
	}

	@Override
	public String getMeterId() {
		return meterId;
	}

	@Override
	public boolean getSubstitute() {
		return substitute;
	}

	@Override
	public String printDateOfReading() {
		 return dateOfReading != null ? dateOfReading.toString() : "Kein Datum Verfügbar";
	}

	@Override
	public UUID getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
		
	}


}
