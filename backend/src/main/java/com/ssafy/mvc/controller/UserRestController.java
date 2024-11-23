package com.ssafy.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.Service;
import com.ssafy.mvc.jwt.JwtUtil;
import com.ssafy.mvc.model.dto.User;
import com.ssafy.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserRestController {
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public UserRestController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	// 사용자 목록 전체 가져오기
	@GetMapping("/users")
	@Operation(summary = "유저 목록 가져오기")
	public ResponseEntity<?> userList() {
		List<User> list = userService.getUserList();
		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}
	
	// id 확인하기
	@GetMapping("/{id}")
	@Operation(summary = "id 확인하기")
	public ResponseEntity<?> getUser(@PathVariable("id") String id) {
		User user = userService.getUserById(id);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// 사용자 회원가입
	@PostMapping("/signup")
	@Operation(summary = "회원 가입")
	public ResponseEntity<String> write(@RequestBody User user) {
		if(userService.getUserById(user.getId())!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID already exists");
		}
		
		if (userService.signup(user)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user");
	}

	// 사용자 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
		HttpStatus status = null;
		Map<String, Object> result = new HashMap<>();
		User loginUser = userService.login(user.getId(), user.getPassword());

		System.out.println(loginUser);
		if (loginUser != null) {
			result.put("message", "login 성공");
			result.put("access-token", jwtUtil.createToken(loginUser.getName()));
			result.put("id", loginUser.getId());
			result.put("name", loginUser.getName());
			result.put("email", loginUser.getEmail());
			result.put("age", loginUser.getAge());
			
			status = HttpStatus.ACCEPTED;
		} else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(result, status);
	}
	
	// 사용자 로그아웃
	
	
	// 유저 업데이트
	@PutMapping("/{id}")
	@Operation(summary = "유저 업데이트")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody User user){
		if(userService.getUserById(id)==null) {
			return new ResponseEntity<String>("해당 ID가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		}
		userService.modifyUser(user);
		return new ResponseEntity<String>("수정 완료",HttpStatus.OK);
	}
	
	// 유저 삭제
	@DeleteMapping("/{id}")
	@Operation(summary = "유저 삭제")
	public ResponseEntity<?> update(@PathVariable("id") String id){
		if(userService.getUserById(id)==null) {
			return new ResponseEntity<String>("해당 ID가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		}
		userService.withdraw(id);
		return new ResponseEntity<String>("삭제 완료",HttpStatus.OK);
	}
}