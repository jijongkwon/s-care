package com.scare.api.member.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scare.api.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MemberInfoDto {

	private String email;
	private String nickname;
	private String provider;
	private String profileUrl;

	@JsonIgnore
	private Long memberId;

	public static MemberInfoDto from(Member member) {
		return MemberInfoDto.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.provider(member.getProvider().name())
			.profileUrl(member.getProfileUrl())
			.build();
	}

}
