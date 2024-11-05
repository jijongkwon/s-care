package com.scare.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.member.domain.Member;
import com.scare.api.member.exception.NoMemberException;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.dto.MemberInfoDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberInfoDto getMemberInfo(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new NoMemberException("존재하지 않는 회원 memberId: " + memberId)
		);

		return MemberInfoDto.from(member);
	}

}
