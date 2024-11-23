package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.Diet;

public interface DietService {
	// id와 날짜 조건 조회 (해당 날짜 식단 조회)
	public List<Diet> getAllByIdAndDate(String id, String weekday);
	
	// id와 날짜와 시간 조건 조회 (해당 날짜 해당 시간 식단 조회)
	public List<Diet> getAllByIdAndDateAndMeal(String id, String weekday, String meal);
	
	// id와 날짜와 음식 조회 (해당 날짜 식단의 특정 음식 조회)
	public Diet getOneByIdAndDateAndFood(Diet diet);
	
	// 날짜와 음식 등록
	public boolean registDiet(Diet diet);
	
	// 음식 수정
	public boolean modifyDiet(Diet diet);
	
	// 일주일 식단 삭제
	public boolean removeAllDiet(String id);
	
	// 해당 요일 삭제
	public boolean removeOneDayDiet(String id, String weekday);
	
	// 해당 요일 해당 시간 삭제
	public boolean removeOneMealDiet(String id, String weekday,String meal);
	
	// 해당 요일 음식 삭제
	public boolean removeOneFoodDiet(Diet diet);
}
