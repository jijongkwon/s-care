package com.scare.api.core.template.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

	// 2000 - 성공
	OK("2000", "성공"),

	// 1000 - SECURITY
	UNAUTHENTICATED("1000", "인증되지 않은 사용자입니다."),
	EXPIRED_JWT_EXCEPTION("1001", "jwt 토큰이 만료되었습니다."),
	UNSUPPORTED_JWT_EXCEPTION("1002", "토큰 발급자가 일치하지 않습니다."),
	UNAUTHORIZED_EXCEPTION("1003", "토큰이 없거나 인증 과정에서 오류가 발생헀습니다."),

	// 3000 - MEMBER
	MEMBER_NOT_FOUND("3000", "회원이 존재하지 않습니다."),
	;

	private String code;
	private String message;

}