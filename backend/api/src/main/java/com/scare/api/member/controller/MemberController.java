package com.scare.api.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.member.controller.docs.MemberControllerDocs;
import com.scare.api.member.service.MemberService;
import com.scare.api.member.service.dto.MemberInfoDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

	private final MemberService memberService;

	@Override
	@GetMapping("/{member-id}")
	public ResponseEntity<BaseResponse<MemberInfoDto>> getMemberInfo(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable("member-id") Long memberId) {

		return ResponseEntity.ok(BaseResponse.ofSuccess(memberService.getMemberInfo(customUserDetails.getMemberId())));
	}

}
