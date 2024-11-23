package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.User;

public interface UserDao {
	// 전체 유저 목록
	public List<User> selectAll();
	
	// 유저 등록 (회원가입 할 때)
	public int insertUser(User user);
	
	// 유저 조회 (로그인할 때)
	public User selectOne(Map<String,String> info);
	
	// 유저 조회 (로그인할 때)
	public User selectOneById(String id);
	
	// 유저 삭제
	public int deleteUser(String id);
	
	// 유저 수정
	public int updateUser(User user);
}
