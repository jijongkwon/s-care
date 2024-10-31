package com.scare.api.member.controller.dto.request;

import lombok.Getter;

@Getter
public class LoginReq {

	private String email;
	private String profileUrl;
	private String nickname;
	private String provider;

}
