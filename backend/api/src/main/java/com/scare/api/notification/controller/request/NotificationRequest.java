package com.scare.api.notification.controller.request;

import com.scare.api.notification.enums.StressStatus;
import com.scare.api.notification.enums.WeatherStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

	@Schema(
		description = "GOOD, MODERATE, BAD 날씨 상태",
		example = "GOOD"
	)
	private WeatherStatus weatherStatus;

	@Schema(
		description = "App FCM Token"
	)
	private String appFcmToken;

	@Schema(
		description = "Watch FCM Token"
	)
	private String watchFcmToken;

	@Schema(
		description = "스트레스가 높으면 true, 낮으면 false",
		example = "HIGH"
	)
	private StressStatus stressStatus;
}
