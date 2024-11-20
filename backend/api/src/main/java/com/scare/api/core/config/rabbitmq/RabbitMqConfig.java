package com.scare.api.core.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Bean
	public Queue requestQueue() {
		return new Queue("request_queue");
	}

	@Bean
	public Queue responseQueue() {
		return new Queue("response_queue");
	}
}

