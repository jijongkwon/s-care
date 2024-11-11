package com.scare.api.solution.walk.service.command.dto;

import com.scare.api.solution.walk.controller.request.command.SaveWalkingCourseReq;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveWalkingCourseLocationDto {

	private double latitude;
	private double longitude;

	public static SaveWalkingCourseLocationDto from(SaveWalkingCourseReq.LocationData location) {
		return SaveWalkingCourseLocationDto.builder()
			.latitude(location.getLatitude())
			.longitude(location.getLongitude())
			.build();
	}
}
