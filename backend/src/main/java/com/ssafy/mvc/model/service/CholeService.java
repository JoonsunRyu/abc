package com.ssafy.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.Chole;

public interface CholeService {
	
	// id와 날짜 조건 조회
	public Chole getOneByIdAndDate(String id, String date);
	
	// id별 전체 조회
	public List<Chole> getCholeListById(String id);
		
	// 등록
	public boolean registChole(Chole chole);
	
	// 수정
	public boolean modifyChole(Chole chole);
	
	// 삭제
	public boolean removeChole(String id, String date);
}
