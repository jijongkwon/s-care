package com.scare.api.member.exception;

public class AlreadyWithdrawnMemberException extends RuntimeException {

	public AlreadyWithdrawnMemberException() {
	}

	public AlreadyWithdrawnMemberException(String message) {
		super(message);
	}

	public AlreadyWithdrawnMemberException(String message, Throwable cause) {
		super(message, cause);
	}

}
