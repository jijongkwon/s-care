package com.scare.api.infrastructure.external.stress;

import java.util.List;

import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseStressDto;

public interface StressApiClient {
	SaveWalkingCourseStressDto getStressData(List<Double> walkingCourseDto, long walkingTime);
}
