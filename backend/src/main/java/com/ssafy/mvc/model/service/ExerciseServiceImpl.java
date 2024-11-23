package com.ssafy.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.ExerciseDao;
import com.ssafy.mvc.model.dto.Exercise;

@Service
public class ExerciseServiceImpl implements ExerciseService{
	
	private final ExerciseDao dao;
	
	public ExerciseServiceImpl(ExerciseDao dao) {
		this.dao= dao;
	}

	@Override
	public Exercise getOneByIdAndDate(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.selectOneByIdAndDate(info);
	}

	@Override
	public boolean registExercise(Exercise exercise) {
		return dao.insertExercise(exercise)==1;
	}

	@Override
	public boolean modifyExercise(Exercise exercise) {
		return dao.updateExercise(exercise)==1;
	}

	@Override
	public boolean removeExercise(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.deleteExercise(info)==1;
	}

	@Override
	public List<Exercise> getAllById(String id) {
		return dao.selectAllById(id);
	}

}
