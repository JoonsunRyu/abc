package com.ssafy.mvc.model.dto;

public class Diet {
	String id;
	String weekday;
	String meal;
	String food;
	
	public Diet() {}

	public Diet(String id, String weekday,String meal, String food) {
		this.id = id;
		this.weekday = weekday;
		this.meal = meal;
		this.food = food;
	}

	public String getId() {
		return id;
	}

	public String getMeal() {
		return meal;
	}

	public void setMeal(String meal) {
		this.meal = meal;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}
	
	
	
}
