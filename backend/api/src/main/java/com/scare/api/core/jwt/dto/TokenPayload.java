package com.scare.api.core.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class TokenPayload {

	private Long memberId;
	private String role;
	private String tokenType;

	public static TokenPayload createAccessTokenPayload(Long memberId, String role) {
		return TokenPayload.builder()
			.memberId(memberId)
			.role(role)
			.tokenType(TokenType.ACCESS_TOKEN.getValue())
			.build();
	}

	public static TokenPayload createRefreshTokenPayload(Long memberId, String role) {
		return TokenPayload.builder()
			.memberId(memberId)
			.role(role)
			.tokenType(TokenType.REFRESH_TOKEN.getValue())
			.build();
	}

}
