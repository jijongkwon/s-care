package com.scare.api.core.config.swagger;

import java.util.Collections;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		// Bearer 인증을 위한 SecurityScheme 설정
		SecurityScheme bearerScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		// SecurityRequirement 설정
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Access Token");

		Server server = new Server();
		server.setUrl("/");
		server.setDescription("Root Server");

		return new OpenAPI()
			.info(new Info().title("S-Care API 문서").version("v1.0"))
			.addSecurityItem(securityRequirement)
			.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("Access Token", bearerScheme))
			.servers(Collections.singletonList(server));
	}

	@Bean
	public GroupedOpenApi v1Api() {
		return GroupedOpenApi.builder()
			.group("API v1 Docs")
			.pathsToMatch("/api/v1/**")  // v1 경로 API 문서 그룹
			.build();
	}

}
