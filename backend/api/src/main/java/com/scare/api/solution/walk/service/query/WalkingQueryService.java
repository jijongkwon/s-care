package com.scare.api.solution.walk.service.query;

import org.springframework.stereotype.Service;

import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;
import com.scare.api.solution.walk.exception.NoWalkingCourseException;
import com.scare.api.solution.walk.exception.NoWalkingDetailException;
import com.scare.api.solution.walk.repository.WalkingCourseRepository;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.query.dto.WalkingCourseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalkingQueryService {

	private final WalkingCourseRepository walkingCourseRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	public WalkingCourseDto getWalkingCourse(Long courseId) {
		WalkingCourse walkingCourse = walkingCourseRepository.findById(courseId)
			.orElseThrow(() -> new NoWalkingCourseException());
		WalkingDetail walkingDetail = walkingDetailRepository.findById(walkingCourse.getId())
			.orElseThrow(() -> new NoWalkingDetailException());
		return WalkingCourseDto.from(walkingCourse, walkingDetail);
	}

}
