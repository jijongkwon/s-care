package com.scare.api.solution.walk.service.command;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
		Member member = MemberServiceHelper.findExistingMember(memberRepository, memberId);
		long walkingTime = getWalkingTime(dto.getStartedAt(), dto.getFinishedAt());

		List<Double> heartRates = dto.getHeartRates();
		List<SaveWalkingCourseLocationDto> walkingLocations = dto.getLocations();

		// 두 리스트 중 더 짧은 길이 계산
		int minSize = Math.min(heartRates.size(), walkingLocations.size());
		heartRates = heartRates.subList(0, minSize);
		walkingLocations = walkingLocations.subList(0, minSize);
		List<WalkingDetail.LocationPoint> locationPoints = processLocationData(walkingLocations);
		try {
			return saveWithStressData(dto, heartRates, walkingTime, member, locationPoints);
		} catch (RestClientException e) {
			log.warn("[ERROR] FAST API 스트레스 데이터 불러오기 실패. 기본 데이터만 저장.", e);
			return saveWithoutStressData(dto, member, locationPoints);
		} catch (Exception e) {
			log.error("[ERROR] 산책 코스 저장 중 예외 발생", e);
			throw new WalkingCourseDataSaveException();
		}
	}

	private long getWalkingTime(LocalDateTime startedAt, LocalDateTime finishedAt) {
		return ChronoUnit.SECONDS.between(startedAt, finishedAt);
	}

	private Long saveWithStressData(SaveWalkingCourseDto dto, List<Double> heartRates, long walkingTime,
		Member member, List<WalkingDetail.LocationPoint> locationPoints) {
		SaveWalkingCourseStressDto stressData = stressApiClient.getStressData(heartRates, walkingTime);
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

	private List<WalkingDetail.LocationPoint> processLocationData(List<SaveWalkingCourseLocationDto> locationPoints) {
		return locationPoints.stream()
			.map(location -> WalkingDetail.LocationPoint.builder()
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.build())
			.collect(Collectors.toList());
	}

	private WalkingCourse createWalkingCourseWithStressData(SaveWalkingCourseDto dto, Member member) {
		return WalkingCourse.builder()
			.minStress(dto.getStressData().getMinStress())
			.maxStress(dto.getStressData().getMaxStress())
			.startIdx(dto.getStressData().getStartIdx() != null ? dto.getStressData().getStartIdx() : 0)
			.endIdx(dto.getStressData().getEndIdx() != null ? dto.getStressData().getEndIdx() : 0)
			.healingStressAvg(
				dto.getStressData().getHealingStressAvg() != null ? dto.getStressData().getHealingStressAvg() : 0)
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