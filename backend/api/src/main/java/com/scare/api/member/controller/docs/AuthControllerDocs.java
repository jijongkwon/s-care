package com.scare.api.member.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.member.controller.dto.request.LoginReq;
import com.scare.api.member.service.dto.LoginDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth Member", description = "Auth API")
public interface AuthControllerDocs {

	@Operation(
		summary = "소셜 로그인",
		description = "소셜 로그인을 통한 회원가입 또는 로그인을 진행합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 또는 로그인 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	ResponseEntity<BaseResponse<LoginDto>> login(@RequestBody LoginReq loginReq, HttpServletResponse response);

	@Operation(
		summary = "AccessToken 재발급",
		description = "RefreshToken을 이용하여 AccessToken을 재발급 받습니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "AccessToken 재발급 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "404", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류"),
	})
	ResponseEntity<BaseResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response);

	@Operation(
		summary = "로그아웃",
		description = "RefreshToken을 만료시킵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "RefreshToken 만료 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "500", description = "서버 오류"),
	})
	ResponseEntity<BaseResponse<?>> logout(HttpServletRequest request);

}