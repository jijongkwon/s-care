package com.scare.api.core.exception.handler;

import static com.scare.api.core.template.response.ResponseCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import com.scare.api.core.template.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class StressExHandler {

	@ExceptionHandler(RestClientException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public BaseResponse<Object> handleServerException(RestClientException e) {
		log.error("[FAST_API_ERROR] 스트레스 데이터 불러오기를 실패했습니다.");
		return BaseResponse.ofFail(FAIL_TO_GET_STRESS_DATE_FAST_API_SERVER);
	}
}
