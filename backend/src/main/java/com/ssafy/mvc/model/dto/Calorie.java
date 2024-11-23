package com.ssafy.mvc.model.dto;

public class Calorie {
	String id;
	String date;
	int calorie;
	
	public Calorie() {}

	public Calorie(String id, String date, int calorie) {
		this.id = id;
		this.date = date;
		this.calorie = calorie;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	
	
}
