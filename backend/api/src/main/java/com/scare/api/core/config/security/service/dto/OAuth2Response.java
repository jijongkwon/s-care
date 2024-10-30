package com.scare.api.core.config.security.service.dto;

import com.scare.api.member.domain.Provider;

public interface OAuth2Response {

	// 제공자
	Provider getProvider();

	// 제공자에서 발급해준 ID
	String getProviderId();

	// 이메일
	String getEmail();

	// 사용자 이름 (닉네임)
	String getNickname();

	// 프로필 이미지
	String getProfileImage();

}
