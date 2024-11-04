package com.scare.api.report.service.walk;

import static com.scare.api.member.service.helper.MemberServiceHelper.*;
import static com.scare.api.report.util.DateConverter.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.report.repository.custom.WalkingReportCustomRepository;
import com.scare.api.report.service.dto.ReportDto;
import com.scare.api.report.service.walk.dto.WalkingOverviewProjection;
import com.scare.api.report.service.walk.dto.WalkingReportDto;
import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;
import com.scare.api.solution.walk.exception.NoWalkingDetailException;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.query.dto.Pos;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalkingReportQueryService {

	private final MemberRepository memberRepository;
	private final WalkingReportCustomRepository walkingReportCustomRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	public ReportDto getWalkingReport(Long memberId, String startDate, String endDate) {
		Member member = findExistingMember(memberRepository, memberId);
		LocalDateTime from = convertToStartOfDay(startDate);
		LocalDateTime to = convertToEndOfDay(endDate);
		WalkingCourse course = walkingReportCustomRepository.getMyBestWalkingCourseBetween(member, from, to);
		WalkingOverviewProjection dto = walkingReportCustomRepository.getMyWalkingOverviewBetween(member, from, to);

		WalkingReportDto.WalkingReportDtoBuilder builder = WalkingReportDto.builder()
			.walkingCnt(dto.getTotalWalkingCnt())
			.totalWalkingTime(dto.getTotalWalkingTime())
			.avgStressChange(dto.getAvgStressChange());

		if (course.hasHealingSection()) {
			WalkingDetail walkingDetail = walkingDetailRepository.findById(course.getId())
				.orElseThrow(() -> new NoWalkingDetailException());
			builder.startIdx(course.getStartIdx())
				.endIdx(course.getEndIdx())
				.posList(getPosList(walkingDetail));
		}

		return builder.build();
	}

	private List<Pos> getPosList(WalkingDetail walkingDetail) {
		return walkingDetail.getLocationData().stream().map(
				locationPoint -> Pos.builder()
					.lat(locationPoint.getLatitude())
					.lng(locationPoint.getLongitude())
					.build()
			)
			.collect(Collectors.toList());
	}

}
