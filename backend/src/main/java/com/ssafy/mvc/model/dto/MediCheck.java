package com.ssafy.mvc.model.dto;

public class MediCheck {
	String id;
	Boolean bloodCheck;
	Boolean medicine;
	String date;
	
	public MediCheck() {}

	public MediCheck(String id, Boolean bloodCheck, Boolean medicine, String date) {
		this.id = id;
		this.bloodCheck = bloodCheck;
		this.medicine = medicine;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getBloodCheck() {
		return bloodCheck;
	}

	public void setBloodCheck(Boolean bloodCheck) {
		this.bloodCheck = bloodCheck;
	}

	public Boolean getMedicine() {
		return medicine;
	}

	public void setMedicine(Boolean medicine) {
		this.medicine = medicine;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
