package com.scare.api.solution.walk.controller.request.command;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "걷기 운동 코스 기록 정보")
public class WalkingCourseReq {

	@Schema(
		description = "걷기 운동 거리(km)",
		example = "1.7"
	)
	private double distance;

	@Schema(
		description = "걷기 운동 시작 시간",
		example = "2024-10-31T14:30:00",
		type = "string",
		format = "date-time"
	)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime startedAt;

	@Schema(
		description = "걷기 운동 종료 시간",
		example = "2024-10-31T15:45:00",
		type = "string",
		format = "date-time"
	)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime finishedAt;

	@Schema(
		description = "스트레스 지수 배열",
		example = "[25.5, 30.2, 28.7, 27.8, 32.1]"
	)
	private List<Double> stressIndices;

	@Schema(description = "위치 데이터 목록")
	private List<LocationData> locations;

	@Getter
	@Builder
	@Schema(description = "위치 데이터")
	public static class LocationData {
		@Schema(
			description = "위도",
			example = "37.5665",
			minimum = "-90",
			maximum = "90"
		)
		private double latitude;

		@Schema(
			description = "경도",
			example = "126.9780",
			minimum = "-180",
			maximum = "180"
		)
		private double longitude;

		@Schema(
			description = "위치 기록 시간",
			example = "2024-10-31T14:30:00",
			type = "string",
			format = "date-time"
		)
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		private LocalDateTime createdAt;
	}
}