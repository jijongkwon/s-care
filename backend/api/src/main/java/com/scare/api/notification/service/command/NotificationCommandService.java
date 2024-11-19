package com.scare.api.notification.service.command;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.scare.api.notification.controller.request.NotificationRequest;
import com.scare.api.notification.enums.NotificationInfo;
import com.scare.api.notification.enums.StressStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationCommandService {

	@Async
	public void sendToWatch(NotificationRequest notificationRequest) {
		log.info("[NotificationService] 워치 알림 보내기 시작");
		sendNotification(notificationRequest.getWatchFcmToken(),
			NotificationInfo.WATCH.getMessage(),
			composeBody(notificationRequest));
	}

	@Async
	public void sendToApp(NotificationRequest notificationRequest) {
		log.info("[NotificationService] 앱 알림 보내기 시작");
		sendNotification(notificationRequest.getAppFcmToken(),
			NotificationInfo.APP.getMessage(),
			composeBody(notificationRequest));
	}

	private void sendNotification(String token, String title, String body) {
		try {
			String response = FirebaseMessaging.getInstance().send(composeMessage(token, title, body));
			log.info("{} 보내기 성공: {}", title, response);
		} catch (FirebaseMessagingException e) {
			throw new RuntimeException("[FCM Exception] Fcm 알림 보내기 실패: ", e);
		}
	}

	private String composeBody(NotificationRequest notificationRequest) {
		if (notificationRequest.getStressStatus() == StressStatus.HIGH) {
			return switch (notificationRequest.getWeatherStatus()) {
				case GOOD, MODERATE -> NotificationInfo.STRESS_HIGH_OUTDOOR.getMessage();
				case BAD -> NotificationInfo.STRESS_HIGH_INDOOR.getMessage();
			};
		}
		return NotificationInfo.STRESS_NORMAL.getMessage();
	}

	private Message composeMessage(String token, String title, String body) {
		return Message.builder()
			.setToken(token)
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();
	}
}
