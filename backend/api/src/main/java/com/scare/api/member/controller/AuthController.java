package com.scare.api.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.member.controller.docs.AuthControllerDocs;
import com.scare.api.member.controller.dto.request.LoginReq;
import com.scare.api.member.service.MemberService;
import com.scare.api.member.service.dto.LoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

	private final MemberService memberService;
	private final JWTUtil jwtUtil;

	@Override
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginDto>> login(@RequestBody LoginReq loginReq, HttpServletResponse response) {
		LoginDto result = memberService.login(loginReq);

		// 토큰 생성
		String accessToken = jwtUtil.createAccessToken(result.getMemberId(), result.getRole());
		log.info("AccessToken 토큰 발급 완료: {}", accessToken);
		String refreshToken = jwtUtil.createRefreshToken();
		log.info("RefreshToken 토큰 발급 완료: {}", refreshToken);

		// 클라이언트에게 JWT 전달 (예: 응답 헤더에 추가)
		response.setHeader("Authorization", "Bearer " + accessToken);
		response.addCookie(jwtUtil.createCookie("refreshToken", refreshToken));

		return ResponseEntity.ok(BaseResponse.ofSuccess(result));
	}

	@Override
	@PostMapping("/reissue")
	public ResponseEntity<BaseResponse<?>> reissue(@CookieValue("refreshToken") String refreshToken,
		HttpServletRequest request,
		HttpServletResponse response) {

		ResponseCode errorResponseCode = jwtUtil.validateToken(refreshToken);

		if (errorResponseCode != null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.ofFail(errorResponseCode));
		}

		// 토큰 생성
		// String accessToken = jwtUtil.createAccessToken(jwt, result.getRole());
		// log.info("AccessToken 토큰 발급 완료: {}", accessToken);
		// String refreshToken = jwtUtil.createRefreshToken();
		// log.info("RefreshToken 토큰 발급 완료: {}", refreshToken);

		// redis에서 값을 가져와서 memberId와 role을 이용해서 토큰 다시 생성
		// 그리고 redis에 다시 넣어야함
		// refreshToken 레디스에 저장
		// 1. 로그아웃 시 블랙아웃 고려?
		// 2. RTR 구현해볼까

		return ResponseEntity.ok(BaseResponse.ofSuccess("재발급은 되는데 redis가 구현이 안 됐음!"));
	}

}
