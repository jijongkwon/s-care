package com.scare.api.solution.walk.service.dto;

import java.time.LocalDateTime;

import com.scare.api.solution.walk.controller.request.WalkingCourseReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalkingCourseLocationDto {

	private double latitude;
	private double longitude;
	private LocalDateTime createdAt;

	public static WalkingCourseLocationDto from(WalkingCourseReq.LocationData location) {
		return WalkingCourseLocationDto.builder()
			.latitude(location.getLatitude())
			.longitude(location.getLongitude())
			.createdAt(location.getCreatedAt())
			.build();
	}

}
