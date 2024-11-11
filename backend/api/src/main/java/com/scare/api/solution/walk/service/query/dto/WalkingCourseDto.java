package com.scare.api.solution.walk.service.query.dto;

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

	private Long courseId;
	private String startedAt;
	private String finishedAt;
	private List<Pos> posList;
	private Integer startIdx;
	private Integer endIdx;
	private double maxStress;
	private double minStress;

	public static WalkingCourseDto from(WalkingCourse walkingCourse, WalkingDetail walkingDetail) {
		return WalkingCourseDto.builder()
			.courseId(walkingCourse.getId())
			.startedAt(walkingCourse.getStartedAt().toString())
			.finishedAt(walkingCourse.getFinishedAt().toString())
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

	public static WalkingCourseDto from(WalkingCourse walkingCourse) {
		return WalkingCourseDto.builder()
			.courseId(walkingCourse.getId())
			.startedAt(walkingCourse.getStartedAt().toString())
			.finishedAt(walkingCourse.getFinishedAt().toString())
			.startIdx(walkingCourse.getStartIdx())
			.endIdx(walkingCourse.getEndIdx())
			.maxStress(walkingCourse.getMaxStress())
			.minStress(walkingCourse.getMinStress())
			.build();
	}

}
