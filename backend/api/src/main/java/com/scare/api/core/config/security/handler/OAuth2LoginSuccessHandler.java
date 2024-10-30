package com.scare.api.core.config.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scare.api.core.config.security.service.dto.CustomOAuth2User;
import com.scare.api.core.jwt.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private final JWTUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		// JWT 토큰 생성
		String accessToken = jwtUtil.createAccessToken(customOAuth2User);
		log.info("AccessToken 토큰 발급 완료: {}", accessToken);
		String refreshToken = jwtUtil.createRefreshToken();
		log.info("RefreshToken 토큰 발급 완료: {}", refreshToken);

		// 클라이언트에게 JWT 전달 (예: 응답 헤더에 추가)
		response.setHeader("Authorization", "Bearer " + accessToken);
		response.addCookie(createCookie("refresh", refreshToken));
		response.setStatus(HttpStatus.OK.value());
	}

	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}

}
