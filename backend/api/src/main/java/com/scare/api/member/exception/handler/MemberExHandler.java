package com.scare.api.member.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.member.exception.AlreadyWithdrawnMemberException;
import com.scare.api.member.exception.InvalidCookieRefreshTokenException;
import com.scare.api.member.exception.NoMemberException;
import com.scare.api.member.exception.RedisTokenStoredException;
import com.scare.api.member.exception.RefreshTokenMismatchException;
import com.scare.api.member.exception.RefreshTokenNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MemberExHandler {

	@ExceptionHandler(NoMemberException.class)
	public ResponseEntity<BaseResponse<Object>> noMemberException(NoMemberException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(BaseResponse.ofFail(ResponseCode.MEMBER_NOT_FOUND));
	}

	@ExceptionHandler(RedisTokenStoredException.class)
	public ResponseEntity<BaseResponse<Object>> redisTokenStoredException(RedisTokenStoredException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(BaseResponse.ofFail(ResponseCode.REDIS_TOKEN_STORED_EXCEPTION));
	}

	@ExceptionHandler(RefreshTokenNotFoundException.class)
	public ResponseEntity<BaseResponse<Object>> refreshTokenNotFoundException(RefreshTokenNotFoundException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(BaseResponse.ofFail(ResponseCode.UNAUTHORIZED_EXCEPTION));
	}

	@ExceptionHandler(RefreshTokenMismatchException.class)
	public ResponseEntity<BaseResponse<Object>> refreshTokenMismatchException(RefreshTokenMismatchException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(BaseResponse.ofFail(e.getResponseCode()));
	}

	@ExceptionHandler(InvalidCookieRefreshTokenException.class)
	public ResponseEntity<BaseResponse<Object>> invalidCookieRefreshTokenException(
		InvalidCookieRefreshTokenException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(BaseResponse.ofFail(ResponseCode.UNAUTHORIZED_EXCEPTION));
	}

	@ExceptionHandler(AlreadyWithdrawnMemberException.class)
	public ResponseEntity<BaseResponse<Object>> alreadyWithdrawnMemberException(AlreadyWithdrawnMemberException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(BaseResponse.ofFail(ResponseCode.ALREADY_WITHDRAWN_MEMBER));
	}

}
