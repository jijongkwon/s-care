package com.scare.api.solution.walk.service.command;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import com.scare.api.infrastructure.external.stress.StressApiClient;
import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.helper.MemberServiceHelper;
import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;
import com.scare.api.solution.walk.exception.WalkingCourseDataSaveException;
import com.scare.api.solution.walk.repository.WalkingCourseRepository;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseDto;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseLocationDto;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseStressDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalkingCommandService {

	private final StressApiClient stressApiClient;
	private final MemberRepository memberRepository;
	private final WalkingCourseRepository walkingCourseRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	@Transactional
	public Long saveWalkingCourse(Long memberId, SaveWalkingCourseDto dto) {
		log.info("[WalkingCommandService] 산책 코스 저장 시작, 산책 시간");
		long walkingTime = getWalkingTime(dto);
		Member member = MemberServiceHelper.findExistingMember(memberRepository, memberId);
		List<WalkingDetail.LocationPoint> locationPoints = processLocationData(dto.getLocations());

		try {
			return saveWithStressData(dto, walkingTime, member, locationPoints);
		} catch (RestClientException e) {
			log.warn("[ERROR] FAST API 스트레스 데이터 불러오기 실패. 기본 데이터만 저장.", e);
			return saveWithoutStressData(dto, member, locationPoints);
		} catch (Exception e) {
			log.error("[ERROR] 산책 코스 저장 중 예외 발생", e);
			throw new WalkingCourseDataSaveException();
		}
	}

	private Long saveWithStressData(SaveWalkingCourseDto dto, long walkingTime,
		Member member, List<WalkingDetail.LocationPoint> locationPoints) {
		SaveWalkingCourseStressDto stressData = stressApiClient.getStressData(dto, walkingTime);
		SaveWalkingCourseDto walkingCourseWithStressDto = dto.withStressData(stressData);

		Long walkingCourseId = walkingCourseRepository.save(
			createWalkingCourseWithStressData(walkingCourseWithStressDto, member)).getId();
		walkingDetailRepository.save(createWalkingCourseDetail(walkingCourseId, locationPoints));
		log.info("[WalkingCommandService] 스트레스 데이터를 포함하여 산책 코스 저장 성공");

		return walkingCourseId;
	}

	private Long saveWithoutStressData(SaveWalkingCourseDto dto,
		Member member, List<WalkingDetail.LocationPoint> locationPoints) {
		Long walkingCourseId = walkingCourseRepository.save(
			createWalkingCourseWthoutStressData(dto, member)).getId();
		walkingDetailRepository.save(createWalkingCourseDetail(walkingCourseId, locationPoints));
		log.info("[WalkingCommandService] 기본 데이터만 산책 코스 저장 성공");

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
				.build());
		}

		return newLocationPoints;
	}

	private WalkingCourse createWalkingCourseWithStressData(SaveWalkingCourseDto dto, Member member) {
		return WalkingCourse.builder()
			.minStress(dto.getStressData().getMinStress())
			.maxStress(dto.getStressData().getMaxStress())
			.startIdx(dto.getStressData().getStartIdx())
			.endIdx(dto.getStressData().getEndIdx())
			.healingStressAvg(dto.getStressData().getHealingStressAvg())
			.startedAt(dto.getStartedAt())
			.finishedAt(dto.getFinishedAt())
			.member(member)
			.build();
	}

	private WalkingCourse createWalkingCourseWthoutStressData(SaveWalkingCourseDto dto, Member member) {
		return WalkingCourse.builder()
			.startedAt(dto.getStartedAt())
			.finishedAt(dto.getFinishedAt())
			.member(member)
			.build();
	}

	private WalkingDetail createWalkingCourseDetail(Long walkingCourseId,
		List<WalkingDetail.LocationPoint> locationPoints) {
		return WalkingDetail.builder()
			.id(walkingCourseId)
			.locationData(locationPoints)
			.build();
	}
}