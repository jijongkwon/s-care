package com.scare.api.report.repository.custom;

import static com.scare.api.solution.walk.domain.QWalkingCourse.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scare.api.member.domain.Member;
import com.scare.api.report.service.walk.dto.QWalkingOverviewProjection;
import com.scare.api.report.service.walk.dto.WalkingOverviewProjection;
import com.scare.api.solution.walk.domain.WalkingCourse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WalkingReportCustomRepository {

	private final JPAQueryFactory queryFactory;

	public WalkingOverviewProjection getMyWalkingOverviewBetween(Member member, LocalDateTime from, LocalDateTime to) {
		NumberTemplate<Integer> totalWalkingTime = Expressions.numberTemplate(Integer.class,
			"COALESCE(SUM(TIME_TO_SEC(TIMEDIFF({0}, {1}))), 0)",
			walkingCourse.finishedAt,
			walkingCourse.startedAt);

		return queryFactory
			.select(new QWalkingOverviewProjection(
				totalWalkingTime,
				walkingCourse.count(),
				walkingCourse.maxStress.subtract(walkingCourse.minStress)
					.abs()
					.avg()
					.coalesce(0.0)
			))
			.from(walkingCourse)
			.where(
				isAfter(from),
				isBefore(to),
				memberEq(member)
			)
			.fetchOne();
	}

	public WalkingCourse getMyBestWalkingCourseBetween(Member member, LocalDateTime from, LocalDateTime to) {
		return queryFactory.selectFrom(walkingCourse)
			.where(
				isAfter(from),
				isBefore(to),
				memberEq(member)
			)
			.orderBy(walkingCourse.maxStress.subtract(walkingCourse.minStress).abs().desc())
			.limit(1)
			.fetchOne();
	}

	private BooleanExpression isBefore(LocalDateTime to) {
		return walkingCourse.finishedAt.loe(to);
	}

	private BooleanExpression memberEq(Member member) {
		return walkingCourse.member.eq(member);
	}

	private BooleanExpression isAfter(LocalDateTime from) {
		return walkingCourse.startedAt.goe(from);
	}

}
