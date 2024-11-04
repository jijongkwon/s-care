package com.scare.api.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.member.controller.docs.AuthControllerDocs;
import com.scare.api.member.controller.dto.request.LoginReq;
import com.scare.api.member.service.AuthService;
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

	private final AuthService authService;
	private final JWTUtil jwtUtil;

	@Override
	@PostMapping("/login")
	public ResponseEntity<BaseResponse<LoginDto>> login(@RequestBody LoginReq loginReq, HttpServletResponse response) {
		LoginDto result = authService.login(LoginDto.from(loginReq));

		// 클라이언트에게 JWT 전달
		response.setHeader("Authorization", "Bearer " + result.getAccessToken());
		response.addCookie(jwtUtil.createCookie("refreshToken", result.getRefreshToken()));

		return ResponseEntity.ok(BaseResponse.ofSuccess(result));
	}

	@Override
	@PostMapping("/reissue")
	public ResponseEntity<BaseResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = authService.reissue(request.getCookies());

		response.setHeader("Authorization", "Bearer " + result.get("accessToken"));
		response.addCookie(jwtUtil.createCookie("refreshToken", result.get("refreshToken")));

		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

	@Override
	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<?>> logout(HttpServletRequest request) {
		authService.logout(request.getCookies());

		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}
}
