package com.scare.api.solution.walk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.domain.WalkingCourse;

public interface WalkingCourseRepository extends JpaRepository<WalkingCourse, Long> {

	Page<WalkingCourse> findAllByMemberOrderByCreatedAtDesc(Member member, PageRequest of);

}
