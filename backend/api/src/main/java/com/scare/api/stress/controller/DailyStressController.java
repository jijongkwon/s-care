package com.scare.api.stress.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.core.util.DateConverter;
import com.scare.api.stress.controller.request.SaveDailyStressReq;
import com.scare.api.stress.service.command.DailyStressCommandService;
import com.scare.api.stress.service.query.dto.DailyStressQueryDto;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/stress")
@RestController
@RequiredArgsConstructor
public class DailyStressController {

	private final DailyStressCommandService dailyStressCommandService;

	@PostMapping("/daily")
	public ResponseEntity<BaseResponse<?>> saveDailyStress(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody SaveDailyStressReq req
	) {
		dailyStressCommandService.saveDailyStress(userDetails.getMemberId(), req.getDailyStressList());
		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}

	@GetMapping("/monthly")
	public ResponseEntity<BaseResponse<List<DailyStressQueryDto>>> getMonthlyStress(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam("from") String from,
		@RequestParam("to") String to
	) {
		List<DailyStressQueryDto> res = dailyStressQueryService.getMonthlyStress(
			userDetails.getMemberId(),
			DateConverter.convertToLocalDate(from),
			DateConverter.convertToLocalDate(to));
		return ResponseEntity.ok(BaseResponse.ofSuccess(res));
	}

}
