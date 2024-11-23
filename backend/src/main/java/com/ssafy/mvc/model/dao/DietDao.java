package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Diet;

public interface DietDao {
	// id와 날짜 조건 조회 (해당 날짜 식단 조회)
	public List<Diet> selectAllByIdAndDate(Map<String,String> info);
	
	// id와 날짜와 시간 조건 조회 (해당 날짜 해당 시간 식단 조회)
	public List<Diet> selectOneByIdAndDateAndMeal(Map<String,String> info);
	
	// id와 날짜와 음식 조회 (해당 날짜 식단의 특정 음식 조회)
	public Diet selectOneByIdAndDateAndFood(Diet diet);
	
	// 날짜와 음식 등록
	public int insertDiet(Diet diet);
	
	// 음식 수정
	public int updateDiet(Diet diet);
	
	// 일주일 식단 삭제
	public int deleteAllDiet(String id);
	
	// 해당 요일 식단 삭제
	public int deleteOneDayDiet(Map<String,String> info);
	
	// 해당 요일 해당 시간 삭제
	public int deleteOneMealDiet(Map<String,String> info);
	
	// 해당 요일 해당 시간 음식 삭제
	public int deleteOneFoodDiet(Diet diet);
}
