package com.scare.api.member.service.helper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scare.api.member.domain.Member;
import com.scare.api.member.exception.NoMemberException;

public final class MemberServiceHelper {

	public static <T extends JpaRepository<Member, Long>> Member findExistingMember(T memberRepository, Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NoMemberException("존재하지 않는 회원 memberId: " + memberId));
	}

}
