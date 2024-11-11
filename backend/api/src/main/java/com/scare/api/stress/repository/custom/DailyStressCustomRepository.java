package com.scare.api.stress.repository.custom;

import static com.scare.api.solution.walk.domain.QWalkingCourse.*;
import static com.scare.api.stress.domain.QDailyStress.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scare.api.member.domain.Member;
import com.scare.api.stress.domain.DailyStress;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DailyStressCustomRepository {

	private final JPAQueryFactory queryFactory;

	public List<DailyStress> findStressBetween(Member member, LocalDate from, LocalDate to) {
		return queryFactory
			.selectFrom(dailyStress)
			.where(
				isAfter(from),
				isBefore(to),
				memberEq(member)
			)
			.fetch();
	}

	private BooleanExpression memberEq(Member member) {
		return walkingCourse.member.eq(member);
	}

	private BooleanExpression isAfter(LocalDate from) {
		return dailyStress.recordedAt.goe(from);
	}

	private BooleanExpression isBefore(LocalDate to) {
		return dailyStress.recordedAt.loe(to);
	}

}
