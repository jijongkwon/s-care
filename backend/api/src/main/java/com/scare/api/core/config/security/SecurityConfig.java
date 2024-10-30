package com.scare.api.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.scare.api.core.config.security.handler.OAuth2LoginSuccessHandler;
import com.scare.api.core.config.security.resolver.CustomAuthorizationRequestResolver;
import com.scare.api.core.config.security.service.CustomOAuth2UserService;
import com.scare.api.core.jwt.JWTFilter;
import com.scare.api.core.jwt.JWTUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ClientRegistrationRepository clientRegistrationRepository;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final JWTUtil jwtUtil;
	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login**", "/oauth2/**", "/api/auth/**", "/api/login/**").permitAll()
				.anyRequest().authenticated())
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorizationEndpoint ->
					authorizationEndpoint.authorizationRequestResolver(customAuthorizationRequestResolver()))
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(oAuth2LoginSuccessHandler)
				.failureHandler((request, response, exception) -> {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.getWriter().write("OAuth2 authentication failed");
				}))
			.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)); // 인증 실패 시 401 반환
			});

		return http.build();
	}

	@Bean
	public CustomAuthorizationRequestResolver customAuthorizationRequestResolver() {
		return new CustomAuthorizationRequestResolver(clientRegistrationRepository);
	}

}
