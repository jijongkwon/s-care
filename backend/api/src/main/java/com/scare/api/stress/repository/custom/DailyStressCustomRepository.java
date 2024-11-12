package com.scare.api.stress.repository.custom;

import static com.scare.api.stress.domain.QDailyStress.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.scare.api.member.domain.Member;
import com.scare.api.report.service.dto.ReportDto;
import com.scare.api.stress.domain.DailyStress;
import com.scare.api.stress.service.command.dto.StressReportDto;

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

	public ReportDto getWeeklyReport(Member member, LocalDate from, LocalDate to) {
		BooleanExpression lastWeekCond = dailyStress.recordedAt.between(from.minusWeeks(1), from.minusDays(1));
		BooleanExpression currentWeekCond = dailyStress.recordedAt.between(from, to);

		Double lastWeekStress = queryFactory.select(dailyStress.stress.avg())
			.from(dailyStress)
			.where(
				lastWeekCond,
				memberEq(member))
			.fetchOne();

		Double currentWeekStress = queryFactory.select(dailyStress.stress.avg())
			.from(dailyStress)
			.where(
				currentWeekCond,
				memberEq(member)
			)
			.fetchOne();

		return StressReportDto.builder()
			.currentWeekStress(currentWeekStress)
			.lastWeekStress(lastWeekStress)
			.build();
	}

	private BooleanExpression memberEq(Member member) {
		return dailyStress.member.eq(member);
	}

	private BooleanExpression isAfter(LocalDate from) {
		return dailyStress.recordedAt.goe(from);
	}

	private BooleanExpression isBefore(LocalDate to) {
		return dailyStress.recordedAt.loe(to);
	}
}
