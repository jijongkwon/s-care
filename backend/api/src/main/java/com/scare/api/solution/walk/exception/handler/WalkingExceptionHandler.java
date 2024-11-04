package com.scare.api.solution.walk.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.solution.walk.exception.WalkingCourseDataSaveException;
import com.scare.api.solution.walk.exception.WalkingCourseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class WalkingExceptionHandler {

	@ExceptionHandler(WalkingCourseDataSaveException.class)
	public ResponseEntity<BaseResponse<?>> handleWalkingCourseDataSaveException(WalkingCourseDataSaveException e) {
		log.error("[WalkingCourseDataSaveException] 산책 코스 데이터 저장 실패", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(BaseResponse.ofFail(ResponseCode.FAIL_TO_SAVE_WALKING_COURSE));
	}

	@ExceptionHandler(WalkingCourseException.class)
	public ResponseEntity<BaseResponse<?>> handleWalkingCourseException(WalkingCourseException e) {
		log.error("[WalkingCourseException] 산책 코스 관련 오류 발생", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(BaseResponse.ofFail(ResponseCode.FAIL_TO_WALKING_COURSE));
	}
}