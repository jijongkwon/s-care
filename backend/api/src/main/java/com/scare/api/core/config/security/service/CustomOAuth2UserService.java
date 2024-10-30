package com.scare.api.core.config.security.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.core.config.security.service.dto.CustomOAuth2User;
import com.scare.api.core.config.security.service.dto.GoogleResponse;
import com.scare.api.core.config.security.service.dto.OAuth2Response;
import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;
import com.scare.api.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 부모 클래스의 메서드를 사용하여 객체를 생성함
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 제공자에 맞는 객체 받아오기
		OAuth2Response oAuth2Response = getOAuth2Response(oAuth2User, userRequest.getClientRegistration().getRegistrationId());

		// 넘어온 회원정보가 이미 우리 DB 테이블에 존재하는지 확인
		Provider provider = oAuth2Response.getProvider();
		Member member = memberRepository.findByEmailAndProvider(oAuth2Response.getEmail(), provider).orElse(null);

		if (member == null) {
			log.info("회원 없음 (회원가입 진행) -> email: {}, provider: {}", oAuth2Response.getEmail(), provider.name());

			member = saveMember(oAuth2Response, provider);

		} else {
			log.info("회원 있음 (회원정보 업데이트) -> email: {}, provider: {}", oAuth2Response.getEmail(), provider.name());

			// 멤버 정보 업데이트
			member.updateNicknameAndProfileUrl(oAuth2Response.getNickname(), oAuth2Response.getProfileImage());
		}

		return CustomOAuth2User.from(member);
	}

	private OAuth2Response getOAuth2Response(OAuth2User oAuth2User, String registrationId) {
		// 제공자에 맞는 객체
		OAuth2Response oAuth2Response = null;

		// 제공자 확인
		if ("google".equals(registrationId)) {
			oAuth2Response = GoogleResponse.builder()
				.attribute(oAuth2User.getAttributes())
				.build();
		}

		return oAuth2Response;
	}

	private Member saveMember(OAuth2Response oAuth2Response, Provider provider) {
		Member member = Member.builder()
			.email(oAuth2Response.getEmail())
			.profileUrl(oAuth2Response.getProfileImage())
			.nickname(oAuth2Response.getNickname())
			.provider(provider)
			.build();

		return memberRepository.save(member);
	}

}
