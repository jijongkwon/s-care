package com.scare.api.stress.controller.request;

import java.util.List;

import com.scare.api.stress.service.command.dto.DailyStressCommandDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveDailyStressReq {

	private List<DailyStressCommandDto> dailyStressList;

}
