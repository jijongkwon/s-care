package com.scare.api.member.service;

import static com.scare.api.member.service.helper.MemberServiceHelper.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return MemberInfoDto.from(findExistingMember(memberRepository, memberId));
	}

}
