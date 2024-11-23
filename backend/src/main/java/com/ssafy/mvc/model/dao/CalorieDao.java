package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Calorie;

public interface CalorieDao {
	// id 전체 데이터
	public List<Calorie> selectAllById(String id);
	
	// id와 날짜 조건 조회
	public Calorie selectOneByIdAndDate(Map<String,String> info);
	
	// id별 기간별 전체 칼로리 조회
	public Integer selectTotalCalorieDate(Map<String,String> info);
	
	// 등록
	public int insertCalorie(Calorie calorie);
	
	// 수정
	public int updateCalorie(Calorie calorie);
	
	// 삭제
	public int deleteCalorie(Map<String,String> info);
}
