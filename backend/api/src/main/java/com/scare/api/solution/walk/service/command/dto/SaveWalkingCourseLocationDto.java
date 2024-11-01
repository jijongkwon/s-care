package com.scare.api.solution.walk.service.command.dto;

import java.time.LocalDateTime;

import com.scare.api.solution.walk.controller.request.command.SaveWalkingCourseReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveWalkingCourseLocationDto {

	private double latitude;
	private double longitude;
	private LocalDateTime createdAt;

	public static SaveWalkingCourseLocationDto from(SaveWalkingCourseReq.LocationData location) {
		return SaveWalkingCourseLocationDto.builder()
			.latitude(location.getLatitude())
			.longitude(location.getLongitude())
			.createdAt(location.getCreatedAt())
			.build();
	}
}
