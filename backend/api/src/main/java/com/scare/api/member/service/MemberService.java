package com.scare.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;
import com.scare.api.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public Member findByEmailAndAuthProvider(String email, Provider provider) {
		// Custom Exception 으로 변경해야 함
		return memberRepository.findByEmailAndProvider(email, provider)
			.orElseThrow(() -> new IllegalArgumentException("Member not found with email, provider: " + email + ", " + provider));
	}

}
