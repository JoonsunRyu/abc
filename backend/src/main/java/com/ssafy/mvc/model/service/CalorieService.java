package com.ssafy.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Calorie;

public interface CalorieService {
	// id 전체 데이터
	public List<Calorie> getAllById(String id);
	
	// id와 날짜 조건 조회
	public Calorie getOneByIdAndDate(String id, String date);
	
	// id별 기간별 전체 칼로리 조회
	public Integer getTotalCalorieDate(String id, String startDate, String endDate);
	
	// 등록
	public boolean registCalorie(Calorie calorie);
	
	// 수정
	public boolean modifyCalorie(Calorie calorie);
	
	// 삭제
	public boolean removeCalorie(String id, String date);
}
