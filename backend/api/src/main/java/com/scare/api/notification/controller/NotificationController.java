package com.scare.api.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.notification.controller.docs.NotificationControllerDocs;
import com.scare.api.notification.controller.request.NotificationRequest;
import com.scare.api.notification.service.command.NotificationCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController implements NotificationControllerDocs {

	private final NotificationCommandService notificationCommandService;

	@PostMapping("/forwarding")
	public ResponseEntity<BaseResponse<?>> sendNotifications(NotificationRequest request) {
		notificationCommandService.sendToWatch(request);
		notificationCommandService.sendToApp(request);
		return ResponseEntity.ok(BaseResponse.ofSuccess());
	}
}
