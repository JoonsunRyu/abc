package com.ssafy.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.MediCheck;

public interface MediCheckService {
	
	// id, 날짜 조건 조회
	public MediCheck getOneByIdAndDate(String id, String date);
	
	// id별 전체 조회
	public List<MediCheck> getMediCheckListById(String id);
		
	// 등록
	public boolean registMediCheck(MediCheck medicheck);
	
	// 수정
	public boolean modifyMediCheck(MediCheck medicheck);
	
	// 삭제
	public boolean removeMediCheck(String id, String date);
}
