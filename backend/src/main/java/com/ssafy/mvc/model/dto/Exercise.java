package com.ssafy.mvc.model.dto;

public class Exercise {
	String id;
	String date;
	String exer;
	
	public Exercise() {}

	public Exercise(String id, String date, String exer) {
		this.id = id;
		this.date = date;
		this.exer = exer;
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

	public String getExer() {
		return exer;
	}

	public void setExer(String exer) {
		this.exer = exer;
	}
	
	
}
