package com.scare.api.member.exception;

public class RefreshTokenNotFoundException extends RuntimeException {

	public RefreshTokenNotFoundException() {
	}

	public RefreshTokenNotFoundException(String message) {
		super(message);
	}

	public RefreshTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
