package com.scare.api.solution.walk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scare.api.solution.walk.domain.WalkingCourse;

public interface WalkingCourseRepository extends JpaRepository<WalkingCourse, Long> {
}
