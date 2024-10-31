package com.scare.api.solution.walk.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WalkingCourseDto {

	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
	private List<Pos> posList;
	private Integer startIdx;
	private Integer endIdx;
	private double maxStress;
	private double minStress;
	private double distance;

	public static WalkingCourseDto from(WalkingCourse walkingCourse, WalkingDetail walkingDetail) {
		return WalkingCourseDto.builder()
			.distance(walkingCourse.getDistance())
			.startedAt(walkingCourse.getStartedAt())
			.finishedAt(walkingCourse.getFinishedAt())
			.startIdx(walkingCourse.getStartIdx())
			.endIdx(walkingCourse.getEndIdx())
			.maxStress(walkingCourse.getMaxStress())
			.minStress(walkingCourse.getMinStress())
			.posList(
				walkingDetail.getLocationData().stream()
					.map(locationData -> Pos.builder()
						.lat(locationData.getLatitude())
						.lng(locationData.getLongitude())
						.build())
					.collect(Collectors.toList())
			)
			.build();
	}

}
