package com.scare.api.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginReq {

	@Schema(description = "소셜 로그인 이메일", example = "user@example.com")
	private String email;

	@Schema(description = "소셜 로그인 프로필 이미지 경로", example = "https://example.com")
	private String profileUrl;

	@Schema(description = "사용자 닉네임(소셜 로그인 이름)", example = "exampleName")
	private String nickname;

	@Schema(description = "소셜 로그인 제공자", example = "google")
	private String provider;

}
