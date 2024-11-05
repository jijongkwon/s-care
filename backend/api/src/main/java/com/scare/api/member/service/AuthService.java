package com.scare.api.member.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.core.jwt.dto.TokenPayload;
import com.scare.api.core.jwt.dto.TokenType;
import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.core.template.response.ResponseCode;
import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;
import com.scare.api.member.exception.RedisTokenStoredException;
import com.scare.api.member.exception.RefreshTokenMismatchException;
import com.scare.api.member.exception.RefreshTokenNotFoundException;
import com.scare.api.member.repository.AuthRepository;
import com.scare.api.member.service.dto.LoginDto;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;
	private final JWTUtil jwtUtil;
	private final RedisTemplate<String, Object> tokenRedisTemplate;

	@Value("${jwt.refresh.expiration}")
	private Long refreshExpiration;

	@Transactional
	public LoginDto login(LoginDto loginDto) {
		// 회원가입 or 로그인 진행
		Member member = processLogin(loginDto);

		// 토큰 생성
		String accessToken = jwtUtil.createToken(
			TokenPayload.createAccessTokenPayload(member.getId(), member.getRole().name()));
		log.info("AccessToken 토큰 발급 완료: {}", accessToken);
		String refreshToken = jwtUtil.createToken(
			TokenPayload.createRefreshTokenPayload(member.getId(), member.getRole().name()));
		log.info("RefreshToken 토큰 발급 완료: {}", refreshToken);

		// 기존 refreshToken 삭제
		deleteRefreshToken(member.getId());

		// refreshToken 레디스에 저장
		saveRefreshToken(refreshToken);

		return LoginDto.from(member, accessToken, refreshToken);
	}

	@Transactional
	public Map<String, Object> reissue(Cookie[] cookies) {
		String refreshToken = jwtUtil.getCookieValue(cookies, "refreshToken");

		validateRefreshToken(refreshToken);

		Long memberId = jwtUtil.getMemberId(refreshToken);
		String role = jwtUtil.getRole(refreshToken);

		String newAccessToken = jwtUtil.createToken(TokenPayload.createAccessTokenPayload(memberId, role));
		log.info("AccessToken 토큰 재발급 완료: {}", newAccessToken);
		String newRefreshToken = jwtUtil.createToken(TokenPayload.createRefreshTokenPayload(memberId, role));
		log.info("RefreshToken 토큰 재발급 완료: {}", newRefreshToken);

		// refreshToken 레디스에 저장
		saveRefreshToken(newRefreshToken);

		return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
	}

	public void logout(Cookie[] cookies) {
		String refreshToken = jwtUtil.getCookieValue(cookies, "refreshToken");

		validateRefreshToken(refreshToken);
	}

	private Member processLogin(LoginDto loginDto) {
		Provider provider = Provider.valueOf(loginDto.getProvider().toUpperCase());

		Member member = authRepository.findByEmailAndProvider(loginDto.getEmail(), provider).orElse(null);

		if (member == null) {
			log.info("회원 없음 (회원가입 진행) -> email: {}, provider: {}", loginDto.getEmail(), provider.name());

			member = authRepository.save(Member.builder()
				.email(loginDto.getEmail())
				.profileUrl(loginDto.getProfileUrl())
				.nickname(loginDto.getNickname())
				.provider(provider)
				.build());

		} else {
			log.info("회원 있음 (회원정보 업데이트) -> email: {}, provider: {}", loginDto.getEmail(), provider.name());

			member.updateNicknameAndProfileUrl(loginDto.getNickname(), loginDto.getProfileUrl());
		}

		return member;
	}

	private void validateRefreshToken(String refreshToken) {
		ResponseCode responseCode = jwtUtil.validateToken(refreshToken, TokenType.REFRESH_TOKEN);
		if (responseCode != null) {
			throw new RefreshTokenMismatchException("[RefreshTokenMismatchException] Cookie Refresh Token 검증 오류",
				responseCode);
		}

		Long memberId = jwtUtil.getMemberId(refreshToken);

		try {
			Object value = tokenRedisTemplate.opsForValue().get("refreshToken:" + memberId);

			if (value == null) {
				throw new RefreshTokenNotFoundException("[RefreshTokenNotFoundException] Redis에 Refresh Token 없음");
			}

			if (!refreshToken.equals((String)value)) {
				throw new RefreshTokenMismatchException(
					"[RefreshTokenMismatchException] Redis와 Cookie간 Refresh Token 불일치");
			}

			deleteRefreshToken(memberId);

		} catch (Exception e) {
			throw new RedisTokenStoredException("[RedisTokenStoredException] Refresh Token 조회 중 Exception 발생");
		}
	}

	private void saveRefreshToken(String refreshToken) {
		Long memberId = jwtUtil.getMemberId(refreshToken);

		try {
			tokenRedisTemplate.opsForValue()
				.set("refreshToken:" + memberId, refreshToken,
					Duration.ofSeconds(refreshExpiration));

			log.info("Redis에 회원 ID: {}에 대한 Refresh Token을 저장하였습니다.", memberId);

		} catch (Exception e) {
			throw new RedisTokenStoredException("[RedisTokenStoredException] Refresh Token 저장 중 Exception 발생");
		}
	}

	private void deleteRefreshToken(Long memberId) {
		try {
			Boolean result = tokenRedisTemplate.delete("refreshToken:" + memberId);

			if (result == null || !result) {
				log.info("Redis에 회원 ID: {}에 대한 Refresh Token이 존재하지 않습니다.", memberId);
			} else {
				log.info("Redis에서 회원 ID: {}에 대한 Refresh Token이 성공적으로 제거되었습니다.", memberId);
			}

		} catch (Exception e) {
			throw new RedisTokenStoredException("[RedisTokenStoredException] Refresh Token 삭제 중 Exception 발생");
		}
	}

}
