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

import com.ssafy.mvc.model.dto.Diet;
import com.ssafy.mvc.model.service.DietService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/diet")
@CrossOrigin("*")
public class DietRestController {
	
	private final DietService service;

	public DietRestController(DietService service) {
		this.service = service;
	}
	
	// id와 날짜 조건 조회 (해당 날짜 식단 조회)
	@GetMapping("/{id}/{weekday}")
	@Operation(summary = "id와 날짜 조건 조회 (해당 날짜 식단 조회)")
	public ResponseEntity<?> dietList(@PathVariable("id") String id, @PathVariable("weekday") String weekday) {
		List<Diet> list = service.getAllByIdAndDate(id, weekday);
		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Diet>>(list, HttpStatus.OK);
	}
	
	// id와 날짜와 시간 조건 조회 (해당 날짜 해당 시간 식단 조회)
	@GetMapping("/{id}/{weekday}/{meal}")
	@Operation(summary = "id와 날짜와 시간 조건 조회 (해당 날짜 해당 시간 식단 조회)")
	public ResponseEntity<?> mealList(@PathVariable("id") String id, @PathVariable("weekday") String weekday,@PathVariable("meal") String meal) {
		List<Diet> list = service.getAllByIdAndDateAndMeal(id, weekday, meal);
		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Diet>>(list, HttpStatus.OK);
	}
	
	
	// id와 날짜와 음식 조회 (해당 날짜 식단의 특정 음식 조회)
	@GetMapping()
	@Operation(summary = "해당 날짜 식단의 특정 음식 조회")
	public ResponseEntity<?> dietOne(@RequestBody Diet diet) {
		Diet one = service.getOneByIdAndDateAndFood(diet);
		if (one == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<Diet>(one, HttpStatus.OK);
	}
	// 날짜와 음식 등록
	@PostMapping()
	@Operation(summary = "날짜와 음식 등록")
	public ResponseEntity<?> insertOne(@RequestBody Diet diet) {
		
		if(service.getOneByIdAndDateAndFood(diet)!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재합니다");
		}
		
		if (service.registDiet(diet)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("등록 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
	}
	
	// 음식 수정
	@PutMapping
	@Operation(summary = "수정")
	public ResponseEntity<String> modify(@RequestBody Diet diet){
		if (service.modifyDiet(diet)) {
			return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	}
	
	// 일주일 식단 삭제
	@DeleteMapping("/{id}")
	@Operation(summary = "일주일 식단 삭제")
	public ResponseEntity<String> removeAll(@PathVariable("id") String id){
		if (service.removeAllDiet(id)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
	
	
	// 해당 요일 식단 삭제
	@DeleteMapping("/{id}/{weekday}")
	@Operation(summary = "해당 요일 식단 삭제")
	public ResponseEntity<String> removeOneDay(@PathVariable("id") String id, @PathVariable("weekday") String weekday){
		if (service.removeOneDayDiet(id, weekday)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
	
	// 해당 요일 해당 시간 식단 삭제
	@DeleteMapping("/{id}/{weekday}/{meal}")
	@Operation(summary = "해당 요일 해당 시간 식단 삭제")
	public ResponseEntity<String> removeOneDay(@PathVariable("id") String id, @PathVariable("weekday") String weekday, @PathVariable("meal") String meal){
		if (service.removeOneMealDiet(id, weekday, meal)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
	
	// 해당 요일 음식 삭제
	@DeleteMapping()
	@Operation(summary = "해당 요일 음식 삭제")
	public ResponseEntity<String> removeOneFood(@RequestBody Diet diet){
		if (service.removeOneFoodDiet(diet)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
	
}
