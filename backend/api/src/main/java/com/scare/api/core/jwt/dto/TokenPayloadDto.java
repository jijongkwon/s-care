package com.scare.api.core.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenPayloadDto {

	private Long memberId;
	private String role;

}
