package com.scare.api.member.exception;

public class InvalidCookieRefreshTokenException extends RuntimeException {

	public InvalidCookieRefreshTokenException() {
	}

	public InvalidCookieRefreshTokenException(String message) {
		super(message);
	}

	public InvalidCookieRefreshTokenException(String message, Throwable cause) {
		super(message, cause);
	}

}
