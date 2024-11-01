package com.scare.api.member.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth Member", description = "Auth API")
public interface AuthControllerDocs {

	@Operation(
		summary = "걷기 운동 기록 저장",
		description = "사용자의 걷기 운동 기록을 저장합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 또는 로그인 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	ResponseEntity<?> login();

	@Operation(
		summary = "산책 코스 상세조회",
		description = "특정 산책 코스의 상세정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "산책 코스 상세조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류"),

	})
	ResponseEntity<?> reissue(Long courseId);

}