package org.itp.dto;

import java.time.LocalDate;

import org.itp.enums.KindOfMeter;
import org.itp.interfaces.ICustomer;
import org.itp.interfaces.IReading;

public class Reading implements IReading{
	
	private String comment;
    private ICustomer customer;
    private LocalDate dateOfReading;
    private KindOfMeter kindOfMeter;
    private double meterCount;
    private String meterId;
    private boolean substitute;

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public void setCustomer(ICustomer customer) {
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
	public ICustomer getCustomer() {
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

}
