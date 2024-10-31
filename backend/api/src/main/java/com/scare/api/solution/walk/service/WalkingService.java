package com.scare.api.solution.walk.service;

import org.springframework.stereotype.Service;

import com.scare.api.solution.walk.repository.WalkingCourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalkingService {

	private final WalkingCourseRepository walkingCourseRepository;
}
