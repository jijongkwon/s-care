package com.scare.api.member.exception;

import com.scare.api.core.template.response.ResponseCode;

import lombok.Getter;

@Getter
public class RefreshTokenMismatchException extends RuntimeException {

	private final ResponseCode responseCode;

	public RefreshTokenMismatchException() {
		this.responseCode = null; // 기본값
	}

	public RefreshTokenMismatchException(String message) {
		super(message);
		this.responseCode = null;
	}

	public RefreshTokenMismatchException(String message, ResponseCode responseCode) {
		super(message);
		this.responseCode = responseCode;
	}

	public RefreshTokenMismatchException(String message, Throwable cause, ResponseCode responseCode) {
		super(message, cause);
		this.responseCode = responseCode;
	}

}
