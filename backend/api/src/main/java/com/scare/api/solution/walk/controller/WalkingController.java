package com.scare.api.solution.walk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.controller.docs.WalkingControllerDocs;
import com.scare.api.solution.walk.controller.request.command.WalkingCourseReq;
import com.scare.api.solution.walk.service.command.WalkingCommandService;
import com.scare.api.solution.walk.service.query.WalkingQueryService;
import com.scare.api.solution.walk.service.query.dto.WalkingCourseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walking")
public class WalkingController implements WalkingControllerDocs {

	private final WalkingCommandService walkingCommandService;
	private final WalkingQueryService walkingQueryService;

	@Override
	@PostMapping("/")
	public ResponseEntity<BaseResponse<?>> saveWalkingCourse(Member member, WalkingCourseReq walkingCourseReq) {
		return ResponseEntity.ok(BaseResponse.ofSuccess(walkingCommandService.saveWalkingCourse(member,
			com.scare.api.solution.walk.service.command.dto.WalkingCourseDto.from(walkingCourseReq))));
	}

	@GetMapping("/detail/{course-id}")
	@Override
	public ResponseEntity<BaseResponse<WalkingCourseDto>> getWalkingCourse(
		@PathVariable("course-id") Long courseId) {
		return ResponseEntity.ok(BaseResponse.ofSuccess(walkingQueryService.getWalkingCourse(courseId)));
	}

}