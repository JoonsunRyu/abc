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

import com.ssafy.mvc.model.dto.Exercise;
import com.ssafy.mvc.model.service.ExerciseService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/exercise")
@CrossOrigin("*")
public class ExerciseRestController {
	private final ExerciseService service;
	
	public ExerciseRestController(ExerciseService service) {
		this.service = service;
	}
	
	// id 전체 조건 조회
	@GetMapping("/{id}")
	@Operation(summary = "id 전체 조건 조회")
	public ResponseEntity<?> getExerciseList(@PathVariable("id") String id) {
		List<Exercise> exercise = service.getAllById(id);
		if (exercise == null||exercise.size()==0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Exercise>>(exercise, HttpStatus.OK);
	}
	
	// id와 날짜 조건 조회
	@GetMapping("/{id}/{date}")
	@Operation(summary = "id와 날짜 조건 조회")
	public ResponseEntity<?> getExercise(@PathVariable("id") String id, @PathVariable("date") String date) {
		Exercise exercise = service.getOneByIdAndDate(id, date);
		if (exercise == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<Exercise>(exercise, HttpStatus.OK);
	}
	
	// 등록
	@PostMapping
	@Operation(summary = "등록")
	public ResponseEntity<String> write(@RequestBody Exercise exercise) {
		
		String checkId = exercise.getId();
		String checkDate = exercise.getDate();
		
		if(service.getOneByIdAndDate(checkId, checkDate)!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재합니다");
		}
		
		if (service.registExercise(exercise)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("등록 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
	}
	
	// 수정
	@PutMapping
	@Operation(summary = "수정")
	public ResponseEntity<String> modify(@RequestBody Exercise exercise){
		if (service.modifyExercise(exercise)) {
			return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	}
	
	// 삭제
	@DeleteMapping
	@Operation(summary = "삭제")
	public ResponseEntity<String> remove(@RequestBody Exercise exercise){
		String id = exercise.getId();
		String date = exercise.getDate();
		if (service.removeExercise(id, date)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
}
