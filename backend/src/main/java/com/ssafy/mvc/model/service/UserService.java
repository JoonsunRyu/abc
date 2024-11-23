package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.User;

public interface UserService {
	//전체 사용자 목록 불러오기
	public List<User> getUserList();
	
	//id로 찾은 사용자 불러오기
	public User getUserById(String id);
	
	//회원가입
	public boolean signup(User user);
	
	//로그인 하기
	public User login(String id, String password);
	
	//회원 탈퇴
	public boolean withdraw(String id);
	
	//회원 정보 수정
	public boolean modifyUser(User user);

}
