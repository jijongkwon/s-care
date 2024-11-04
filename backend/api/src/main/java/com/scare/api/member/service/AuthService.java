package com.scare.api.member.service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.core.jwt.dto.TokenPayloadDto;
import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;
import com.scare.api.member.exception.RefreshTokenDataNotFoundException;
import com.scare.api.member.exception.RefreshTokenExpiredException;
import com.scare.api.member.exception.RefreshTokenStoredException;
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
	private final RedisTemplate<String, Object> refreshTokenRedisTemplate;

	@Value("${jwt.refresh.expiration}")
	private Long refreshExpiration;

	@Transactional
	public LoginDto login(LoginDto loginDto) {
		// 회원가입 or 로그인 진행
		Member member = processLogin(loginDto);

		// 토큰 생성
		TokenPayloadDto tokenPayloadDto = TokenPayloadDto.builder()
			.memberId(member.getId())
			.role(member.getRole().name())
			.build();

		String accessToken = jwtUtil.createAccessToken(tokenPayloadDto);
		log.info("AccessToken 토큰 발급 완료: {}", accessToken);
		String refreshToken = jwtUtil.createRefreshToken();
		log.info("RefreshToken 토큰 발급 완료: {}", refreshToken);

		// refreshToken 레디스에 저장
		saveRefreshToken(tokenPayloadDto, refreshToken);
		log.info("Redis에 Refresh Token 저장 완료");

		return LoginDto.from(member, accessToken, refreshToken);
	}

	@Transactional
	public Map<String, String> reissue(Cookie[] cookies) {
		String refreshTokenKey = "refreshToken:" + jwtUtil.getCookieValue(cookies, "refreshToken");

		Map<Object, Object> refreshTokenDetails = findRefreshToken(refreshTokenKey);

		Long memberId = Optional.ofNullable(refreshTokenDetails.get("memberId"))
			.map(value -> Long.valueOf(value.toString()))
			.orElseThrow(() -> new RefreshTokenDataNotFoundException("memberId를 찾을 수 없습니다."));

		String role = Optional.ofNullable(refreshTokenDetails.get("role"))
			.map(value -> value.toString())
			.orElseThrow(() -> new RefreshTokenDataNotFoundException("role을 찾을 수 없습니다."));

		deleteRefreshToken(refreshTokenKey);

		TokenPayloadDto tokenPayloadDto = TokenPayloadDto.builder()
			.memberId(memberId)
			.role(role)
			.build();

		String accessToken = jwtUtil.createAccessToken(tokenPayloadDto);
		log.info("AccessToken 토큰 재발급 완료: {}", accessToken);
		String refreshToken = jwtUtil.createRefreshToken();
		log.info("RefreshToken 토큰 재발급 완료: {}", refreshToken);

		// refreshToken 레디스에 저장
		saveRefreshToken(tokenPayloadDto, refreshToken);
		log.info("Redis에 Refresh Token 저장 완료");

		return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
	}

	public void logout(Cookie[] cookies) {
		String refreshTokenKey = "refreshToken:" + jwtUtil.getCookieValue(cookies, "refreshToken");

		deleteRefreshToken(refreshTokenKey);
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

	private void saveRefreshToken(TokenPayloadDto tokenPayloadDto, String refreshToken) {
		String refreshTokenKey = "refreshToken:" + refreshToken;

		try {
			refreshTokenRedisTemplate.opsForHash().putAll(
				refreshTokenKey,
				Map.of(
					"memberId", String.valueOf(tokenPayloadDto.getMemberId()),
					"role", tokenPayloadDto.getRole()
				)
			);
			refreshTokenRedisTemplate.expire(refreshTokenKey, Duration.ofSeconds(refreshExpiration));

		} catch (Exception e) {
			throw new RefreshTokenStoredException("Redis에 Refresh Token 저장 중 Exception 발생");
		}
	}

	private Map<Object, Object> findRefreshToken(String refreshTokenKey) {
		try {
			Map<Object, Object> refreshTokenDetails = refreshTokenRedisTemplate.opsForHash().entries(refreshTokenKey);

			if (refreshTokenDetails.isEmpty()) {
				throw new RefreshTokenExpiredException("Redis에 Refresh Token 없음");
			}

			return refreshTokenDetails;

		} catch (Exception e) {
			throw new RefreshTokenStoredException("Redis에서 Refresh Token 조회 중 Exception 발생");
		}
	}

	private void deleteRefreshToken(String refreshTokenKey) {
		try {
			refreshTokenRedisTemplate.delete(refreshTokenKey);

		} catch (Exception e) {
			throw new RefreshTokenStoredException("Redis에서 Refresh Token 삭제 중 Exception 발생");
		}
	}

}
