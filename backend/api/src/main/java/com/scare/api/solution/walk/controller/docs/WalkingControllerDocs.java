package com.scare.api.solution.walk.controller.docs;

import org.springframework.http.ResponseEntity;

import com.scare.api.core.template.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Walking", description = "Walking API")
public interface WalkingControllerDocs {

	@Operation(
		summary = "걷기 운동 기록 저장",
		description = "사용자의 걷기 운동 기록을 저장합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "걷기 운동 기록 저장 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	ResponseEntity<BaseResponse<?>> saveWalkingCourse();

}