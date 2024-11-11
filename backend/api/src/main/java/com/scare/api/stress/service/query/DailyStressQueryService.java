package com.scare.api.stress.service.query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.helper.MemberServiceHelper;
import com.scare.api.stress.domain.DailyStress;
import com.scare.api.stress.repository.custom.DailyStressCustomRepository;
import com.scare.api.stress.service.query.dto.DailyStressQueryDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyStressQueryService {

	private final MemberRepository memberRepository;
	private final DailyStressCustomRepository dailyStressCustomRepository;

	public List<DailyStressQueryDto> getMonthlyStress(Long memberId, LocalDate from, LocalDate to) {
		Member member = MemberServiceHelper.findExistingMember(memberRepository, memberId);
		List<DailyStress> stressList = dailyStressCustomRepository.findStressBetween(member, from, to);
		Map<LocalDate, Integer> map = stressList.stream()
			.collect(Collectors.toMap(DailyStress::getRecordedAt, DailyStress::getStress));
		List<DailyStressQueryDto> res = new ArrayList<>();

		for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
			int stress = map.getOrDefault(date, -1);
			res.add(DailyStressQueryDto.builder()
				.stress(stress)
				.recordedAt(date.toString())
				.build());
		}

		return res;
	}

}
