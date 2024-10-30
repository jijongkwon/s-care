package com.scare.api.core.config.security.service.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.scare.api.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomOAuth2User implements OAuth2User {

	private Long memberId;
	private String email;
	private String role;

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
				return role;
			}
		});

		return collection;
	}

	@Override
	public String getName() {
		return email;
	}

	public static CustomOAuth2User from(Member member) {
		return CustomOAuth2User.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.role(member.getRole().name())
			.build();
	}

}
