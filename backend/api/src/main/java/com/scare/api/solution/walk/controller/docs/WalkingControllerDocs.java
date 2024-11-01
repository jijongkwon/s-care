package com.scare.api.solution.walk.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.solution.walk.controller.request.command.SaveWalkingCourseReq;
import com.scare.api.solution.walk.service.query.dto.WalkingCourseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Walking", description = "Walking API")
public interface WalkingControllerDocs {

	@Operation(
		summary = "걷기 운동 기록 저장",
		description = "사용자의 걷기 운동 기록을 저장합니다. (10분 이상 시 기록)"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "걷기 운동 기록 저장 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	ResponseEntity<BaseResponse<?>> saveWalkingCourse(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		SaveWalkingCourseReq saveWalkingCourseReq
	);

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
	ResponseEntity<BaseResponse<WalkingCourseDto>> getWalkingCourse(Long courseId);

}