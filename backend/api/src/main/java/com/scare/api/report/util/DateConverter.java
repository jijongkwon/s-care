package com.scare.api.report.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateConverter {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static LocalDateTime convertToStartOfDay(String dateStr) {
		LocalDate date = LocalDate.parse(dateStr, FORMATTER);
		return date.atTime(0, 0, 0); // 자정으로 LocalDateTime 생성
	}

	public static LocalDateTime convertToEndOfDay(String dateStr) {
		LocalDate date = LocalDate.parse(dateStr, FORMATTER);
		return date.atTime(23, 59, 59); // 하루의 마지막 시간으로 설정
	}

}
