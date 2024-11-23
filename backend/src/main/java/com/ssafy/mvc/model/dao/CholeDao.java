package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Chole;
import com.ssafy.mvc.model.dto.MediCheck;

public interface CholeDao {
	
	// id와 날짜 조건 조회
	public Chole selectOneByIdAndDate(Map<String,String> info);
	
	// id별 전체 조회
	public List<Chole> selectAllById(String id);
	
	// 등록
	public int insertChole(Chole chole);
	
	// 수정
	public int updateChole(Chole chole);
	
	// 삭제
	public int deleteChole(Map<String,String> info);
}
