package com.scare.api.infrastructure.rabbitmq;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class RabbitMqProducer {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper; // JSON 변환기

	public void sendMessage(Long courseId, Long walkingTime, List<Double> heartRates) {
		try {
			Map<String, Object> message = Map.of(
				"courseId", courseId,
				"heartRates", heartRates,
				"walkingTime", walkingTime
			);

			String jsonMessage = objectMapper.writeValueAsString(message);

			rabbitTemplate.convertAndSend("request_queue", jsonMessage);
		} catch (Exception e) {
			throw new RuntimeException("Failed to send message to RabbitMQ", e);
		}
	}
}
