package com.scare.api.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.member.controller.docs.AuthControllerDocs;
import com.scare.api.member.controller.dto.request.LoginReq;
import com.scare.api.member.service.AuthService;
import com.scare.api.member.service.dto.LoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

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
		Map<String, Object> result = authService.reissue(request.getCookies());

		Object errorResponseCode = result.getOrDefault("errorResponseCode", null);
		if (errorResponseCode != null) {
			return ResponseEntity.ok(BaseResponse.ofFail((ResponseCode)errorResponseCode));
		}

		response.setHeader("Authorization", "Bearer " + result.get("accessToken"));
		response.addCookie(jwtUtil.createCookie("refreshToken", (String)result.get("refreshToken")));

		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

	@Override
	@PostMapping("/logout")
	public ResponseEntity<BaseResponse<?>> logout(HttpServletRequest request) {
		authService.logout(request.getCookies());

		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

	@Override
	@PatchMapping("/{member-id}/withdraw")
	public ResponseEntity<BaseResponse<?>> withdraw(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable("member-id") Long memberId,
		HttpServletRequest request) {

		authService.withdraw(customUserDetails.getMemberId(), request.getCookies());

		return null;
	}
}
