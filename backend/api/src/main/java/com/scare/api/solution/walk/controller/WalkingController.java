package com.scare.api.solution.walk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.solution.walk.controller.docs.WalkingControllerDocs;
import com.scare.api.solution.walk.controller.request.command.SaveWalkingCourseReq;
import com.scare.api.solution.walk.service.command.WalkingCommandService;
import com.scare.api.solution.walk.service.command.dto.SaveWalkingCourseDto;
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
	@PostMapping
	public ResponseEntity<BaseResponse<?>> saveWalkingCourse(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody SaveWalkingCourseReq saveWalkingCourseReq) {
		return ResponseEntity.ok(BaseResponse.ofSuccess(
			walkingCommandService.saveWalkingCourse(customUserDetails.getMemberId(),
				SaveWalkingCourseDto.from(saveWalkingCourseReq))));
	}

	@GetMapping("/detail/{course-id}")
	@Override
	public ResponseEntity<BaseResponse<WalkingCourseDto>> getWalkingCourse(
		@PathVariable("course-id") Long courseId) {
		return ResponseEntity.ok(BaseResponse.ofSuccess(walkingQueryService.getWalkingCourse(courseId)));
	}

	@GetMapping("/list")
	@Override
	public ResponseEntity<BaseResponse<List<WalkingCourseDto>>> getWalkingCourseList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam("page") int page,
		@RequestParam("size") int size) {
		return ResponseEntity.ok(BaseResponse.ofSuccess(walkingQueryService.getWalkingCourseList(
			userDetails.getMemberId(), page, size)));
	}

}