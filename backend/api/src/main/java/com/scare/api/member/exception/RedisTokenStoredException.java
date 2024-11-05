package com.scare.api.member.exception;

public class RedisTokenStoredException extends RuntimeException {

	public RedisTokenStoredException() {
	}

	public RedisTokenStoredException(String message) {
		super(message);
	}

	public RedisTokenStoredException(String message, Throwable cause) {
		super(message, cause);
	}

}
