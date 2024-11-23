package com.ssafy.mvc.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.CholeDao;
import com.ssafy.mvc.model.dto.Chole;
@Service
public class CholeServiceImpl implements CholeService{
	
	private final CholeDao dao;
	
	public CholeServiceImpl(CholeDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Chole> getCholeListById(String id) {
		return dao.selectAllById(id);
	}

	@Override
	public boolean registChole(Chole chole) {
		return dao.insertChole(chole)==1;
	}

	@Override
	public boolean modifyChole(Chole chole) {
		return dao.updateChole(chole)==1;
	}

	@Override
	public boolean removeChole(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.deleteChole(info)==1;
	}

	@Override
	public Chole getOneByIdAndDate(String id, String date) {
		Map<String, String> info = new HashMap<>();
		info.put("id", id);
		info.put("date", date);
		return dao.selectOneByIdAndDate(info);
	}

}
