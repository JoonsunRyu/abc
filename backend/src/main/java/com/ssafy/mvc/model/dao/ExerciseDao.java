package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Exercise;

public interface ExerciseDao {
	// id 전체 조회
	public List<Exercise> selectAllById(String id);
	
	// id와 날짜 조건 조회
	public Exercise selectOneByIdAndDate(Map<String,String> info);
	
	// 등록
	public int insertExercise(Exercise exercise);
	
	// 수정
	public int updateExercise(Exercise exercise);
	
	// 삭제
	public int deleteExercise(Map<String,String> info);
	
}
