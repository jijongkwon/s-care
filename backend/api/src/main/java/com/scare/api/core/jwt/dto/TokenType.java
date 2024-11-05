package com.scare.api.core.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {

	ACCESS_TOKEN("accessToken"),
	REFRESH_TOKEN("refreshToken");

	private String value;

}
