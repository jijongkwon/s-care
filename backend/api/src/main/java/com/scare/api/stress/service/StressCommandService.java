package com.scare.api.stress.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scare.api.core.util.DateConverter;
import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.helper.MemberServiceHelper;
import com.scare.api.stress.domain.DailyStress;
import com.scare.api.stress.repository.DailyStressRepository;
import com.scare.api.stress.service.dto.DailyStressDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StressCommandService {

	private final MemberRepository memberRepository;
	private final DailyStressRepository dailyStressRepository;

	public void saveDailyStress(Long memberId, List<DailyStressDto> dtoList) {
		Member member = MemberServiceHelper.findExistingMember(memberRepository, memberId);
		List<DailyStress> stressList = dtoList.stream()
			.map(dto -> DailyStress.builder()
				.stress(dto.getStress())
				.recordedAt(DateConverter.convertToLocalDate(dto.getRecordedAt()))
				.member(member)
				.build())
			.collect(Collectors.toList());
		dailyStressRepository.saveAll(stressList);
	}

}
