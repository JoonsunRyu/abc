package com.ssafy.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.MediCheckDao;
import com.ssafy.mvc.model.dto.MediCheck;

@Service
public class MediCheckServiceImpl implements MediCheckService{
	
	private final MediCheckDao dao;
	
	public MediCheckServiceImpl(MediCheckDao dao) {
		this.dao = dao;
	}

	@Override
	public List<MediCheck> getMediCheckListById(String id) {
		return dao.selectAllById(id);
	}

	@Override
	public boolean registMediCheck(MediCheck medicheck) {
		return dao.insertMediCheck(medicheck)==1;
	}

	@Override
	public boolean modifyMediCheck(MediCheck medicheck) {
		return dao.updateMediCheck(medicheck)==1;
	}

	@Override
	public boolean removeMediCheck(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.deleteMediCheck(info)==1;
	}

	@Override
	public MediCheck getOneByIdAndDate(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.selectOneByIdAndDate(info);
	}

}
