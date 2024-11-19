package com.scare.api.core.config.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	@Value("${fast.api.base-url}")
	private String fastAPIURL;

	@Bean
	public RestClient restFastAPIClient() {
		return RestClient.builder()
			.baseUrl(fastAPIURL)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}
