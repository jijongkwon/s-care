package com.scare.api.solution.walk.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Pos {

	private double lat;
	private double lng;

}
