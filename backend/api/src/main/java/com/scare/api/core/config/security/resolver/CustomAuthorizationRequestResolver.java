package com.scare.api.core.config.security.resolver;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/api/login");
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		String uri = request.getRequestURI();

		if (uri.startsWith("/api/login")) {
			request.setAttribute("registrationId", uri.split("/")[3]);
		}

		return defaultResolver.resolve(request);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientId) {
		return defaultResolver.resolve(request, clientId);
	}

}
