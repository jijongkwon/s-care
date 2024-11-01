package com.scare.api.member.service.helper;

import com.scare.api.member.domain.Member;
import com.scare.api.member.exception.NoMemberException;
import com.scare.api.member.repository.MemberRepository;

public final class MemberServiceHelper {

	public static Member findExistingMember(MemberRepository memberRepository, Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NoMemberException());
	}

}
