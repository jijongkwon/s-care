package com.scare.api.solution.walk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.solution.walk.controller.docs.WalkingControllerDocs;

@RestController
@RequestMapping("/api/v1/walking")
public class WalkingController implements WalkingControllerDocs {

	@Override
	public ResponseEntity<BaseResponse<?>> saveWalkingCourse() {
		return null;
	}

}