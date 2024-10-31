package com.scare.api.solution.walk.exception;

public class NoWalkingDetailException extends RuntimeException {

	public NoWalkingDetailException() {
	}

	public NoWalkingDetailException(String message) {
		super(message);
	}

	public NoWalkingDetailException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoWalkingDetailException(Throwable cause) {
		super(cause);
	}

}
