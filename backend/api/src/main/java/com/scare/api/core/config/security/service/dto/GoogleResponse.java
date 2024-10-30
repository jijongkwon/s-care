package com.scare.api.core.config.security.service.dto;

import java.util.Map;

import com.scare.api.member.domain.Provider;

import lombok.Builder;

@Builder
public class GoogleResponse implements OAuth2Response {

	private final Map<String, Object> attribute;

	public GoogleResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public Provider getProvider() {
		return Provider.GOOGLE;
	}

	@Override
	public String getProviderId() {
		// null이거나 그런 경우는 없나!?
		return attribute.get("providerId").toString();
	}

	@Override
	public String getEmail() {
		// null이거나 그런 경우는 없나!?
		return attribute.get("email").toString();
	}

	@Override
	public String getNickname() {
		// null이거나 그런 경우는 없나!?
		return attribute.get("name").toString();
	}

	@Override
	public String getProfileImage() {
		// null이거나 그런 경우는 없나!?
		return attribute.get("picture").toString();
	}

}
