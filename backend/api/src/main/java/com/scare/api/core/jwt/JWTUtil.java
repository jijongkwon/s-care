package com.scare.api.core.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.scare.api.core.config.security.service.dto.CustomOAuth2User;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

	private final SecretKeySpec secretKeySpec;

	@Value("${jwt.access.expiration}")
	private Long accessExpiration;
	@Value("${jwt.refresh.expiration}")
	private Long refreshExpiration;

	public JWTUtil(@Value("${jwt.secret.key}") String secretKey) {
		secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public Long getMemberId(String token) {
		return Jwts.parser().verifyWith(secretKeySpec).build().parseSignedClaims(token).getPayload().get("memberId", Long.class);
	}

	public String getRole(String token) {
		return Jwts.parser().verifyWith(secretKeySpec).build().parseSignedClaims(token).getPayload().get("role", String.class);
	}

	public void validateToken(String token) {
		Jwts.parser().verifyWith(secretKeySpec).build().parseSignedClaims(token).getPayload().getExpiration();
	}

	public String createAccessToken(CustomOAuth2User customOAuth2User) {
		return Jwts.builder()
			.claim("memberId", customOAuth2User.getMemberId())
			.claim("role", customOAuth2User.getRole())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessExpiration))
			.signWith(secretKeySpec)
			.compact();
	}

	public String createRefreshToken() {
		return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
	}

}
