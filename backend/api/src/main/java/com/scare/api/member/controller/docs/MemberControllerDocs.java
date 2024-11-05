package com.scare.api.member.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.member.service.dto.MemberInfoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member", description = "Auth API")
public interface MemberControllerDocs {

	@Operation(
		summary = "회원 정보 조회",
		description = "회원 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	public ResponseEntity<BaseResponse<MemberInfoDto>> getMemberInfo(@PathVariable("memberId") Long memberId);

}
