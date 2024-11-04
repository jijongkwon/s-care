package com.scare.api.member.exception;

public class RefreshTokenStoredException extends RuntimeException {

	public RefreshTokenStoredException() {
	}

	public RefreshTokenStoredException(String message) {
		super(message);
	}

	public RefreshTokenStoredException(String message, Throwable cause) {
		super(message, cause);
	}

}
