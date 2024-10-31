package com.scare.api.core.jwt.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {

	@Value("${jwt.secret.key}")
	private String secretKeyStr;
	private SecretKey secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessExpiration;

	// 테스트용
	// private Long accessExpiration = 10000L; // 10초

	@Value("${jwt.refresh.expiration}")
	private Long refreshExpiration;

	@PostConstruct
	public void init() {
		secretKey = new SecretKeySpec(secretKeyStr.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public Long getMemberId(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("memberId", Long.class);
	}

	public String getRole(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
	}

	public Boolean validateToken(String token, boolean isAccessToken) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();

		} catch (ExpiredJwtException e) {
			log.info("{} Token Expired", isAccessToken ? "Access" : "Refresh");
			// Custom Exception 으로 나중에 변경
			return false;

		} catch (JwtException je) {
			log.info("{} Invalid", isAccessToken ? "Access" : "Refresh");
			// Custom Exception 으로 나중에 변경
			return false;
		}

		return true;
	}

	public String createAccessToken(Long memberId, String role) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("role", role)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessExpiration))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken() {
		return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
	}

	public Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge((int)(refreshExpiration / 1000));
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		return cookie;
	}

	public String getCookieValue(Cookie[] cookies, String key) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

}
