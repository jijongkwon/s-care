package com.scare.api.infrastructure.external.stress.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.scare.api.infrastructure.external.stress.StressApiClient;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseStressDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientStressApiClient implements StressApiClient {

	@Value("${fast.api.stress-endpoint}")
	private String stressEndpoint;

	private final RestClient restClient;

	@Override
	public SaveWalkingCourseStressDto getStressData(List<Double> heartRates, long walkingTime) throws
		RestClientException {
		log.info("[FAST_API_INFO] fast api stress data 받아오기 시작");
		return restClient.post()
			.uri(stressEndpoint)
			.contentType(MediaType.APPLICATION_JSON)
			.body(createBody(heartRates, walkingTime))
			.retrieve()
			.body(SaveWalkingCourseStressDto.class);
	}

	private Map<String, Object> createBody(List<Double> heartRates, long walkingTime) {
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("hr_data", heartRates);
		requestMap.put("walking_time", walkingTime);
		return requestMap;
	}

}
