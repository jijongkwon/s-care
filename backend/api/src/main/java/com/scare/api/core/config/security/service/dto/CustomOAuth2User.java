package com.scare.api.core.config.security.service.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.scare.api.member.domain.Member;

import lombok.Builder;

@Builder
public class CustomOAuth2User implements OAuth2User {

	private final UserInfo userInfo;

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();

		// 람다로 표현 가능하나 그냥 Override 하고 싶었음
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return userInfo.getRole();
			}
		});

		return collection;
	}

	@Override
	public String getName() {
		return userInfo.getEmail();
	}

	public Long getMemberId() {
		return userInfo.getMemberId();
	}

	public String getEmail() {
		return userInfo.getEmail();
	}

	public String getProfileUrl() {
		return userInfo.getProfileUrl();
	}

	public String getNickname() {
		return userInfo.getNickname();
	}

	public String getRole() {
		return userInfo.getRole();
	}

	public static CustomOAuth2User from(Member member) {
		return CustomOAuth2User.builder()
			.userInfo(UserInfo.builder()
				.memberId(member.getId())
				.email(member.getEmail())
				.profileUrl(member.getProfileUrl())
				.nickname(member.getNickname())
				.role(member.getRole().name())
				.build())
			.build();
	}

}
