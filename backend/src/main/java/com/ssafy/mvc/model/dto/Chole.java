package com.ssafy.mvc.model.dto;

public class Chole {
	String id;
	int ldl;
	int hdl;
	String date;
	
	public Chole() {}

	public Chole(String id, int ldl, int hdl, String date) {
		this.id = id;
		this.ldl = ldl;
		this.hdl = hdl;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLdl() {
		return ldl;
	}

	public void setLdl(int ldl) {
		this.ldl = ldl;
	}

	public int getHdl() {
		return hdl;
	}

	public void setHdl(int hdl) {
		this.hdl = hdl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
