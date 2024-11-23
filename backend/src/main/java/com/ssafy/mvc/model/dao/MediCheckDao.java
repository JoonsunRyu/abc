package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.MediCheck;

public interface MediCheckDao {
	
	// id, 날짜 조건 조회
	public MediCheck selectOneByIdAndDate(Map<String,String> info);
	
	// id별 전체 조회
	public List<MediCheck> selectAllById(String id);
	
	// 등록
	public int insertMediCheck(MediCheck medicheck);
	
	// 수정
	public int updateMediCheck(MediCheck medicheck);
	
	// 삭제
	public int deleteMediCheck(Map<String,String> info);
}
