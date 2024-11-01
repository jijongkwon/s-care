package com.scare.api.core.jwt.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.template.response.ResponseCode;

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
	private final ObjectMapper objectMapper;
	private static final List<String> EXCLUDED_URIS = List.of("/api/v1/members/auth/login",
		"/api/v1/members/auth/reissue"
	);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader("Authorization");

		// 토큰이 없다면 다음 필터로 넘김
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = authorizationHeader.substring(7);

		// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		ResponseCode errorResponseCode = jwtUtil.validateToken(accessToken);
		if (errorResponseCode != null) {
			setErrorResponse(response, errorResponseCode);
			return;
		}

		// Authentication 저장
		setAuthentication(accessToken);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return EXCLUDED_URIS.contains(request.getRequestURI());
	}

	private void setErrorResponse(HttpServletResponse response, ResponseCode errorResponseCode) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// JSON 형태의 오류 응답 작성
		response.getWriter().write(objectMapper.writeValueAsString(BaseResponse.ofFail(errorResponseCode)));
	}

	private void setAuthentication(String accessToken) {
		// memberId, role 값을 획득
		Long memberId = jwtUtil.getMemberId(accessToken);
		String role = jwtUtil.getRole(accessToken);

		CustomUserDetails customUserDetails = CustomUserDetails.builder()
			.memberId(memberId)
			.role(role)
			.build();

		// 토큰으로 부터 인증 정보 추출
		Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());

		// 강제로 시큐리티의 세션에 접근하여 값 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
