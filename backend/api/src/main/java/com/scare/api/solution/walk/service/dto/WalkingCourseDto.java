package com.scare.api.solution.walk.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.scare.api.solution.walk.controller.request.WalkingCourseReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalkingCourseDto {

	private double distance;
	private double minStress;
	private double maxStress;
	private int startIdx;
	private int endIdx;
	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
	private WalkingCourseStressDto stressData;
	private List<WalkingCourseLocationDto> locations;

	public static WalkingCourseDto from(WalkingCourseReq request) {
		return WalkingCourseDto.builder()
			.distance(request.getDistance())
			.startedAt(request.getStartedAt())
			.finishedAt(request.getFinishedAt())
			.stressData(WalkingCourseStressDto.from(request.getStressIndices()))
			.locations(request.getLocations().stream()
				.map(WalkingCourseLocationDto::from)
				.collect(Collectors.toList()))
			.build();
	}
}
