package com.scare.api.core.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.scare.api.core.config.security.service.dto.CustomOAuth2User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");

		// 토큰이 없다면 다음 필터로 넘김
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = authorizationHeader.substring(7);

		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		try {
			jwtUtil.validateToken(accessToken);

		} catch (ExpiredJwtException e) {
			log.info("Access Token Expired");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			// Custom Exception 으로 나중에 변경
			return;

		} catch (JwtException je) {
			log.info("Access Token Invalid");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			// Custom Exception 으로 나중에 변경
			return;
		}

		// memberId, role 값을 획득
		Long memberId = jwtUtil.getMemberId(accessToken);
		String role = jwtUtil.getRole(accessToken);

		CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
			.memberId(memberId)
			.role(role)
			.build();

		// 토큰으로 부터 인증 정보 추출
		Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

		// 강제로 시큐리티의 세션에 접근하여 값 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

}
