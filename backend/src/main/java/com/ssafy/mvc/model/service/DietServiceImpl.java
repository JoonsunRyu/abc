package com.ssafy.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.DietDao;
import com.ssafy.mvc.model.dto.Diet;

@Service
public class DietServiceImpl implements DietService{
	
	private final DietDao dao;
	
	public DietServiceImpl(DietDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Diet> getAllByIdAndDate(String id, String weekday) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("weekday", weekday);
		return dao.selectAllByIdAndDate(info);
	}

	@Override
	public Diet getOneByIdAndDateAndFood(Diet diet) {
		return dao.selectOneByIdAndDateAndFood(diet);
	}

	@Override
	public boolean registDiet(Diet diet) {
		return dao.insertDiet(diet)==1;
	}

	@Override
	public boolean modifyDiet(Diet diet) {
		return dao.updateDiet(diet)==1;
	}

	@Override
	public boolean removeAllDiet(String id) {
		return dao.deleteAllDiet(id)>0;
	}

	@Override
	public boolean removeOneDayDiet(String id, String weekday) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("weekday", weekday);
		return dao.deleteOneDayDiet(info)>0;
	}

	@Override
	public boolean removeOneFoodDiet(Diet diet) {
		return dao.deleteOneFoodDiet(diet)==1;
	}

	@Override
	public boolean removeOneMealDiet(String id, String weekday, String meal) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("weekday", weekday);
		info.put("meal", meal);
		return dao.deleteOneMealDiet(info)==1;
	}

	@Override
	public List<Diet> getAllByIdAndDateAndMeal(String id, String weekday, String meal) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("weekday", weekday);
		info.put("meal", meal);
		return dao.selectOneByIdAndDateAndMeal(info);
	}
}
