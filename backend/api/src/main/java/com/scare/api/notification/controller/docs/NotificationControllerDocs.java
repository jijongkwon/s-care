package com.scare.api.notification.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.notification.controller.request.NotificationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface NotificationControllerDocs {

	@Operation(
		summary = "알림 서비스",
		description = """
			1. stress 가 높고 날씨가 좋으면 -> 산책 추천
			2. stress 가 높고 날씨가 좋지 않으면 -> 실내 활동 추천
			"""
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "알림 보내기 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "500", description = "서버 오류"),
	})
	ResponseEntity<BaseResponse<?>> sendNotifications(
		@RequestBody NotificationRequest notificationRequest
	);
}
