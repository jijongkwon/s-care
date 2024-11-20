package com.scare.api.infrastructure.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scare.api.infrastructure.rabbitmq.dto.ResponseMessage;
import com.scare.api.solution.walk.service.command.WalkingCommandService;
import com.scare.api.solution.walk.service.command.dto.UpdateBestSectionDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqConsumer {

	private final WalkingCommandService walkingCommandService;
	private final ObjectMapper objectMapper;

	@RabbitListener(queues = "response_queue")
	public void consumeMessage(String message) {
		try {
			ResponseMessage response = objectMapper.readValue(message, ResponseMessage.class);

			log.info("[RabbitMQ Consumer] courseId={}", response.getCourseId());
			log.info("[RabbitMQ Consumer] maxStress={}", response.getMaxStress());
			log.info("[RabbitMQ Consumer] minStress={}", response.getMinStress());
			log.info("[RabbitMQ Consumer] healingStressAvg={}", response.getHealingStressAvg());
			log.info("[RabbitMQ Consumer] startIdx={}", response.getStartIdx());
			log.info("[RabbitMQ Consumer] endIdx={}", response.getEndIdx());

			walkingCommandService.updateBestSection(UpdateBestSectionDto.builder()
				.courseId(response.getCourseId())
				.maxStress(response.getMaxStress())
				.minStress(response.getMinStress())
				.healingStressAvg(response.getHealingStressAvg())
				.startIdx(response.getStartIdx())
				.endIdx(response.getEndIdx())
				.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}