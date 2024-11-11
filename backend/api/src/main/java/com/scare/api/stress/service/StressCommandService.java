package com.scare.api.stress.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.scare.api.core.util.DateConverter;
import com.scare.api.stress.domain.DailyStress;
import com.scare.api.stress.service.dto.DailyStressDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StressCommandService {

	public void saveDailyStress(Long memberId, List<DailyStressDto> dtoList) {
		List<DailyStress> stressList = dtoList.stream()
			.map(dto -> DailyStress.builder().stress(dto.getStress())
				.recordedAt(DateConverter.convertToLocalDate(dto.getRecordedAt()))
				.build())
			.collect(Collectors.toList());

	}

}
