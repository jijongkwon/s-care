package com.scare.api.solution.walk.service.command.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WalkingCourseStressDto {
	private List<Double> indices;

	public static WalkingCourseStressDto from(List<Double> indices) {
		return WalkingCourseStressDto.builder()
			.indices(indices)
			.build();
	}
}
