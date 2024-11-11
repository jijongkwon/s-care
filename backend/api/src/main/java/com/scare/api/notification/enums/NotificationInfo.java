package com.scare.api.notification.enums;

import lombok.Getter;

@Getter
public enum NotificationInfo {

	WATCH("워치 알림"),
	APP("앱 알림"),
	STRESS_HIGH_OUTDOOR("스트레스가 높네요. 산책을 추천합니다!"),
	STRESS_HIGH_INDOOR("스트레스가 높네요. 실내활동을 추천합니다!"),
	STRESS_NORMAL("스트레스 상태가 양호합니다. 현재 날씨를 즐기세요!");

	private final String message;

	NotificationInfo(String message) {
		this.message = message;
	}
}
