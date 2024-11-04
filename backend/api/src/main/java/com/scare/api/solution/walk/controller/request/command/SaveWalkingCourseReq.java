package com.scare.api.solution.walk.controller.request.command;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "걷기 운동 코스 기록 정보")
public class SaveWalkingCourseReq {

	@Schema(
		description = "걷기 운동 거리(km)",
		example = "1.7"
	)
	private double distance;

	@Schema(
		description = "걷기 운동 시작 시간",
		example = "2024-10-31T14:30:00",
		type = "string"
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime startedAt;

	@Schema(
		description = "걷기 운동 종료 시간",
		example = "2024-10-31T15:45:00",
		type = "string"
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime finishedAt;

	@Schema(
		description = "심박수 목록",
		example = "[25, 30, 28, 27, 32]"
	)
	private List<Integer> heartRates;

	@Schema(description = "위치 데이터 목록")
	private List<LocationData> locations;

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
			type = "string"
		)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		private LocalDateTime createdAt;
	}
}