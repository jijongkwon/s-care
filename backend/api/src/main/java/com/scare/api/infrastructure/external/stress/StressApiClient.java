package com.scare.api.infrastructure.external.stress;

import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseDto;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseStressDto;

public interface StressApiClient {
	SaveWalkingCourseStressDto getStressData(SaveWalkingCourseDto walkingCourseDto, long walkingTime);
}
