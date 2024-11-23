package com.ssafy.mvc.controller;

import java.util.List;

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

import com.ssafy.mvc.model.dto.Calorie;
import com.ssafy.mvc.model.service.CalorieService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/calorie")
@CrossOrigin("*")
public class CalorieRestController {
	
	private final CalorieService service;
	
	public CalorieRestController(CalorieService service) {
		this.service = service;
	}
	
	// id와 날짜 조건 조회
	@GetMapping("/{id}")
	@Operation(summary = "id 전체 조회")
	public ResponseEntity<?> getCalorieList(@PathVariable("id") String id) {
		List<Calorie> calorie = service.getAllById(id);
		if (calorie == null || calorie.size()==0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity <List<Calorie>>(calorie, HttpStatus.OK);
	}
	
	// id와 날짜 조건 조회
	@GetMapping("/{id}/{date}")
	@Operation(summary = "id와 날짜 조건 조회")
	public ResponseEntity<?> getCalorie(@PathVariable("id") String id, @PathVariable("date") String date) {
		Calorie calorie = service.getOneByIdAndDate(id, date);
		if (calorie == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<Calorie>(calorie, HttpStatus.OK);
	}
	
	// id별 기간별 전체 칼로리 조회
	@GetMapping("/{id}/{startDate}/{endDate}")
	@Operation(summary = "id별 기간별 전체 칼로리 조회")
	public ResponseEntity<?> getCalorieBetweenDate(@PathVariable("id") String id, @PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
		int total = service.getTotalCalorieDate(id, startDate, endDate);
		return new ResponseEntity<Integer>(total, HttpStatus.OK);
	}
	// 등록
	@PostMapping
	@Operation(summary = "등록")
	public ResponseEntity<String> write(@RequestBody Calorie calorie) {
		
		String checkId = calorie.getId();
		String checkDate = calorie.getDate();
		
		if(service.getOneByIdAndDate(checkId, checkDate)!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재합니다");
		}
		
		if (service.registCalorie(calorie)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("등록 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
	}
	// 수정
	@PutMapping
	@Operation(summary = "수정")
	public ResponseEntity<String> modify(@RequestBody Calorie calorie){
		if (service.modifyCalorie(calorie)) {
			return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	}
	
	// 삭제
	@DeleteMapping
	@Operation(summary = "삭제")
	public ResponseEntity<String> remove(@RequestBody Calorie calorie){
		String id = calorie.getId();
		String date = calorie.getDate();
		if (service.removeCalorie(id, date)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
}
