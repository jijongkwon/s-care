package com.scare.api.solution.walk.service.command;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.infrastructure.external.stress.StressApiClient;
import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.helper.MemberServiceHelper;
import com.scare.api.solution.walk.domain.WalkingDetail;
import com.scare.api.solution.walk.repository.WalkingCourseRepository;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseDto;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseLocationDto;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseStressDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalkingCommandService {

	private final StressApiClient stressApiClient;
	private final MemberRepository memberRepository;
	private final WalkingCourseRepository walkingCourseRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	public Long saveWalkingCourse(CustomUserDetails customUserDetails, SaveWalkingCourseDto walkingCourseSaveDto) {
		long walkingTime = getWalkingTime(walkingCourseSaveDto);
		SaveWalkingCourseStressDto stressData = stressApiClient.getStressData(walkingCourseSaveDto, walkingTime);

		SaveWalkingCourseDto enrichedDto = walkingCourseSaveDto.withStressData(stressData);
		List<WalkingDetail.LocationPoint> locationPoints = processLocationData(enrichedDto.getLocations());
		Member existingMember = MemberServiceHelper.findExistingMember(memberRepository,
			customUserDetails.getMemberId());

		Long walkingCourseId = walkingCourseRepository.save(enrichedDto.toWalkingCourse(existingMember)).getId();
		walkingDetailRepository.save(enrichedDto.toWalkingDetail(walkingCourseId, locationPoints));
		return walkingCourseId;
	}

	private long getWalkingTime(SaveWalkingCourseDto walkingCourseSaveDto) {
		return ChronoUnit.SECONDS.between(
			walkingCourseSaveDto.getStartedAt(),
			walkingCourseSaveDto.getFinishedAt()
		);
	}

	private List<WalkingDetail.LocationPoint> processLocationData(List<SaveWalkingCourseLocationDto> locationPoints) {
		List<WalkingDetail.LocationPoint> newLocationPoints = new ArrayList<>();
		for (int i = 3; i < locationPoints.size(); i++) {
			SaveWalkingCourseLocationDto location = locationPoints.get(i);
			newLocationPoints.add(WalkingDetail.LocationPoint.builder()
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.createdAt(location.getCreatedAt())
				.build());
		}

		return newLocationPoints;
	}
}
