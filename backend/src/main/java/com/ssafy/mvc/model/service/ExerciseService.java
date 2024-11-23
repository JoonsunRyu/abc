package com.ssafy.mvc.model.service;


import java.util.List;

import com.ssafy.mvc.model.dto.Exercise;

public interface ExerciseService {
	
	// id 전체 조회
	public List<Exercise> getAllById(String id);
	
	// id와 날짜 조건 조회
	public Exercise getOneByIdAndDate(String id, String date);
	
	// 등록
	public boolean registExercise(Exercise exercise);
	
	// 수정
	public boolean modifyExercise(Exercise exercise);
	
	// 삭제
	public boolean removeExercise(String id, String date);
}
