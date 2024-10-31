package com.scare.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.core.jwt.util.JWTUtil;
import com.scare.api.member.controller.dto.request.LoginReq;
import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.dto.LoginDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JWTUtil jwtUtil;

	@Transactional
	public LoginDto login(LoginReq loginReq) {
		// 회원가입 or 로그인 진행
		Member member = processLogin(loginReq);

		// refreshToken 레디스에 저장
		// 1. 로그아웃 시 블랙아웃 고려?
		// 2. RTR 구현해볼까

		return LoginDto.from(member);
	}

	private Member processLogin(LoginReq loginReq) {
		Provider provider = Provider.valueOf(loginReq.getProvider().toUpperCase());

		Member member = memberRepository.findByEmailAndProvider(loginReq.getEmail(), provider).orElse(null);

		if (member == null) {
			log.info("회원 없음 (회원가입 진행) -> email: {}, provider: {}", loginReq.getEmail(), provider.name());

			member = memberRepository.save(Member.builder()
				.email(loginReq.getEmail())
				.profileUrl(loginReq.getProfileUrl())
				.nickname(loginReq.getNickname())
				.provider(provider)
				.build());

		} else {
			log.info("회원 있음 (회원정보 업데이트) -> email: {}, provider: {}", loginReq.getEmail(), provider.name());

			member.updateNicknameAndProfileUrl(loginReq.getNickname(), loginReq.getProfileUrl());
		}

		return member;
	}

}
