package com.scare.api.solution.walk.exception;

public class NoWalkingCourseException extends RuntimeException {

	public NoWalkingCourseException() {
	}

	public NoWalkingCourseException(String message) {
		super(message);
	}

	public NoWalkingCourseException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoWalkingCourseException(Throwable cause) {
		super(cause);
	}

}
