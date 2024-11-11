package com.scare.api.stress.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.stress.controller.request.SaveDailyStressReq;
import com.scare.api.stress.service.StressCommandService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/stress")
@RestController
@RequiredArgsConstructor
public class StressController {

	private final StressCommandService stressCommandService;

	@PostMapping("/daily")
	public ResponseEntity<BaseResponse<?>> saveDailyStress(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody SaveDailyStressReq req
	) {
		stressCommandService.saveDailyStress(userDetails.getMemberId(), req.getDailyStressList());
		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

}
