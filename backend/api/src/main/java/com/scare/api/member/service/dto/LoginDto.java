package com.scare.api.member.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scare.api.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginDto {

	private String email;
	private String profileUrl;
	private String nickname;
	private String provider;
	private String role;

	@JsonIgnore
	private Long memberId;

	@JsonIgnore
	private String accessToken;

	@JsonIgnore
	private String refreshToken;

	public static LoginDto from(Member member, String accessToken, String refreshToken) {
		return LoginDto.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.profileUrl(member.getProfileUrl())
			.nickname(member.getNickname())
			.provider(member.getProvider().name())
			.role(member.getRole().name())
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

}
