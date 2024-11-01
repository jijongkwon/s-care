package com.scare.api.solution.walk.service.command;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.repository.WalkingCourseRepository;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.command.dto.WalkingCourseDto;
import com.scare.api.solution.walk.service.command.dto.WalkingCourseStressDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalkingCommandService {

	@Value("${fast.api.stress-endpoint}")
	private String stressEndpoint;

	private final RestClient restClient;

	private final WalkingCourseRepository walkingCourseRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	// TODO: 산책 코스 저장
	public Long saveWalkingCourse(Member member, WalkingCourseDto walkingCourseDto) {
		long walkingTime = getWalkingTime(walkingCourseDto);

		WalkingCourseStressDto stressData = getStressData(walkingCourseDto, walkingTime);
		WalkingCourseDto enrichedDto = walkingCourseDto.withStressData(stressData);
		Long walkingCourseId = walkingCourseRepository.save(enrichedDto.toWalkingCourse()).getId();
		walkingDetailRepository.save(enrichedDto.toWalkingDetail(walkingCourseId));
		return walkingCourseId;
	}

	// TODO: 총 산책 시간 반환
	private long getWalkingTime(WalkingCourseDto walkingCourseDto) {
		return ChronoUnit.SECONDS.between(
			walkingCourseDto.getStartedAt(),
			walkingCourseDto.getFinishedAt()
		);
	}

	//TODO: Fast API 호출
	public WalkingCourseStressDto getStressData(WalkingCourseDto walkingCourseDto, long walkingTime) throws
		RestClientException {
		return restClient.post()
			.uri(stressEndpoint)
			.contentType(MediaType.APPLICATION_JSON)
			.body(createRequestBody(walkingCourseDto, walkingTime))
			.retrieve()
			.body(WalkingCourseStressDto.class);
	}

	private Map<String, Object> createRequestBody(WalkingCourseDto dto, long walkingTime) {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("hr_data", dto.getHeartRates());
		requestBody.put("walking_time", walkingTime);
		return requestBody;
	}
}
