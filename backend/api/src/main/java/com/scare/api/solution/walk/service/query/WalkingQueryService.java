package com.scare.api.solution.walk.service.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scare.api.member.domain.Member;
import com.scare.api.member.repository.MemberRepository;
import com.scare.api.member.service.helper.MemberServiceHelper;
import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;
import com.scare.api.solution.walk.exception.NoWalkingCourseException;
import com.scare.api.solution.walk.exception.NoWalkingDetailException;
import com.scare.api.solution.walk.repository.WalkingCourseRepository;
import com.scare.api.solution.walk.repository.WalkingDetailRepository;
import com.scare.api.solution.walk.service.query.dto.WalkingCourseDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalkingQueryService {

	private final MemberRepository memberRepository;
	private final WalkingCourseRepository walkingCourseRepository;
	private final WalkingDetailRepository walkingDetailRepository;

	public WalkingCourseDto getWalkingCourse(Long courseId) {
		WalkingCourse walkingCourse = walkingCourseRepository.findById(courseId)
			.orElseThrow(() -> new NoWalkingCourseException());
		WalkingDetail walkingDetail = walkingDetailRepository.findById(walkingCourse.getId())
			.orElseThrow(() -> new NoWalkingDetailException());
		return WalkingCourseDto.from(walkingCourse, walkingDetail);
	}

	public List<WalkingCourseDto> getWalkingCourseList(Long memberId, int page, int size) {
		Member member = MemberServiceHelper.findExistingMember(memberRepository, memberId);
		Page<WalkingCourse> courses = walkingCourseRepository.findAllByMemberOrderByCreatedAtDesc(member,
			PageRequest.of(page, size));
		return courses.getContent()
			.stream()
			.map(
				course -> WalkingCourseDto.from(course)
			).collect(Collectors.toList());
	}

}
