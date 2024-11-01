package com.scare.api.core.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi v1Api() {
		return GroupedOpenApi.builder()
			.group("API v1 Docs")
			.pathsToMatch("/api/v1/**")  // v1 경로 API 문서 그룹
			.build();
	}

}
