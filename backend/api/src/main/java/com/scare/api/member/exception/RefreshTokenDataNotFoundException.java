package com.scare.api.member.exception;

public class RefreshTokenDataNotFoundException extends RuntimeException {

	public RefreshTokenDataNotFoundException() {
	}

	public RefreshTokenDataNotFoundException(String message) {
		super(message);
	}

	public RefreshTokenDataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
