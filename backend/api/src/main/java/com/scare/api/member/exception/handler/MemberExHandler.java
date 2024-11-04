package com.scare.api.member.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.member.exception.NoMemberException;
import com.scare.api.member.exception.RefreshTokenDataNotFoundException;
import com.scare.api.member.exception.RefreshTokenExpiredException;
import com.scare.api.member.exception.RefreshTokenStoredException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MemberExHandler {

	@ExceptionHandler(NoMemberException.class)
	public ResponseEntity<BaseResponse<Object>> noMemberException(NoMemberException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(BaseResponse.ofFail(ResponseCode.MEMBER_NOT_FOUND));
	}

	@ExceptionHandler(RefreshTokenStoredException.class)
	public ResponseEntity<BaseResponse<Object>> refreshTokenSaveException(RefreshTokenStoredException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(BaseResponse.ofFail(ResponseCode.REFRESH_TOKEN_STORED_EXCEPTION));
	}

	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<BaseResponse<Object>> refreshTokenExpiredException(RefreshTokenExpiredException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(BaseResponse.ofFail(ResponseCode.UNAUTHORIZED_EXCEPTION));
	}

	@ExceptionHandler(RefreshTokenDataNotFoundException.class)
	public ResponseEntity<BaseResponse<Object>> refreshTokenDataNotFoundException(RefreshTokenDataNotFoundException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(BaseResponse.ofFail(ResponseCode.UNAUTHORIZED_EXCEPTION));
	}
}
