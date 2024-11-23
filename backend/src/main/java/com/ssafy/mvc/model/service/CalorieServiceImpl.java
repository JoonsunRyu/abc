package com.ssafy.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.CalorieDao;
import com.ssafy.mvc.model.dto.Calorie;

@Service
public class CalorieServiceImpl implements CalorieService{
	
	private final CalorieDao dao;
	
	public CalorieServiceImpl(CalorieDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Calorie getOneByIdAndDate(String id, String date) {
		Map<String,String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.selectOneByIdAndDate(info);
	}

	@Override
	public Integer getTotalCalorieDate(String id, String startDate, String endDate) {
		Map<String,String> info = new HashMap<>();
		info.put("id", id);
		info.put("startDate", startDate);
		info.put("endDate", endDate);
		return dao.selectTotalCalorieDate(info);
	}

	@Override
	public boolean registCalorie(Calorie calorie) {
		return dao.insertCalorie(calorie)==1;
	}

	@Override
	public boolean modifyCalorie(Calorie calorie) {
		return dao.updateCalorie(calorie)==1;
	}

	@Override
	public boolean removeCalorie(String id, String date) {
		Map<String,String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.deleteCalorie(info)==1;
	}

	@Override
	public List<Calorie> getAllById(String id) {
		return dao.selectAllById(id);
	}

}
