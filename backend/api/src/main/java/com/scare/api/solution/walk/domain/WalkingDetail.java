package com.scare.api.solution.walk.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Document(collection = "walking_detail")
public class WalkingDetail {

	@Id
	@Field("walking_detail_id")
	private Long id;

	@Field("location_data")
	private List<LocationPoint> locationData;

	@Builder
	public WalkingDetail(Long id, List<LocationPoint> locationData) {
		this.id = id;
		this.locationData = locationData;
	}

	@Getter
	public static class LocationPoint {
		private double latitude;
		private double longitude;

		@Builder
		public LocationPoint(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}
}