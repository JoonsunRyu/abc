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

import com.ssafy.mvc.model.dto.Chole;
import com.ssafy.mvc.model.service.CholeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/chole")
@CrossOrigin("*")
public class CholeRestController {
	private final CholeService service;
	public CholeRestController(CholeService service) {
		this.service = service;
	}
	
	
	// id별 전체 조회
	
	@GetMapping("/{id}")
	@Operation(summary = "id별 전체 목록 가져오기")
	public ResponseEntity<?> choleList(@PathVariable("id") String id) {
		List<Chole> list = service.getCholeListById(id);
		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Chole>>(list, HttpStatus.OK);
	}
	
	// 등록
	@PostMapping
	@Operation(summary = "등록")
	public ResponseEntity<String> write(@RequestBody Chole chole) {
		
		String checkId = chole.getId();
		String checkDate = chole.getDate();
		
		if(service.getOneByIdAndDate(checkId, checkDate)!=null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재합니다");
		}
		
		if (service.registChole(chole)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("등록 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
	}
	
	// 수정
	@PutMapping
	@Operation(summary = "수정")
	public ResponseEntity<String> modify(@RequestBody Chole chole){
		if (service.modifyChole(chole)) {
			return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	}
	
	// 삭제
	@DeleteMapping
	@Operation(summary = "삭제")
	public ResponseEntity<String> remove(@RequestBody Chole chole){
		String id = chole.getId();
		String date = chole.getDate();
		if (service.removeChole(id, date)) {
			return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
	}
}
