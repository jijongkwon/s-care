package com.scare.api.member.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scare.api.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginDto {

	@JsonIgnore
	private Long memberId;

	private String email;
	private String profileUrl;
	private String nickname;
	private String provider;
	private String role;

	public static LoginDto from(Member member) {
		return LoginDto.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.profileUrl(member.getProfileUrl())
			.nickname(member.getNickname())
			.provider(member.getProvider().name())
			.role(member.getRole().name())
			.build();
	}

}
