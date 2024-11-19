package com.scare.api.core.jwt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements Serializable {

	private final Long memberId;
	private final String role;

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

}
