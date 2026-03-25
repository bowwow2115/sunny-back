package com.sunny.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunny.model.*;
import com.sunny.model.dto.BusinessHistoryDto;
import com.sunny.model.dto.HistorySearchCondition;
import com.sunny.repository.HistoryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.sunny.model.QBusinessHistory.businessHistory;

@Repository
@RequiredArgsConstructor
@Transactional
public class HistoryRepositoryCustomImpl implements HistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByCreatedDate(LocalDateTime createdDate) {
        if(createdDate == null) {
            return;
        }

        queryFactory.delete(businessHistory)
                .where(businessHistory.createdDate.before(createdDate))
                .execute();
    }

    @Override
    public List<BusinessHistory> findByTargetIdAndType(Long targetId, String targetType) {
        return queryFactory.selectFrom(businessHistory)
                .where(businessHistory.targetId.eq(targetId)
                .and(businessHistory.targetType.eq(targetType)))
                .fetch();
    }

    @Override
    public long deleteByIds(List<Long> ids) {
        return queryFactory
                .delete(businessHistory)
                .where(businessHistory.id.in(ids))
                .execute();
    }

    /**
     * cutoffDate 이전에 생성된 히스토리의 아이디를 batchSize 만큼 조회
     * @param cutoffDate
     * @param batchSize
     * @return
     */
    @Override
    public List<Long> findOldHistory(LocalDateTime cutoffDate, int batchSize) {
        return queryFactory
                .select(businessHistory.id)
                .from(businessHistory)
                .where(businessHistory.createdDate.lt(cutoffDate))
                .orderBy(businessHistory.id.asc())
                .limit(batchSize)
                .fetch();
    }

    /**
     * 최근(자동생성된 아이디 기준) 생성된 히스토리를 페이지네이션하여 조회
     * targetType 별로 조회 가능함
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<BusinessHistoryDto> findHistoryByCondition(Pageable pageable,
                                                           HistorySearchCondition condition) {
        BooleanExpression typeCondition = targetTypeEq(condition.getTargetType());
        return switch (condition.getTargetType()) {
            case "Child" -> fetchWithChild(businessHistory, typeCondition, condition, pageable);
            case "Parents" -> fetchWithParentsJoin(businessHistory, typeCondition, condition, pageable);
            case "SunnyClass" -> fetchWithClassJoin(businessHistory, typeCondition, condition, pageable);
            case "SunnyRide" -> fetchWithRideJoin(businessHistory, typeCondition, condition, pageable);
            case "User" -> fetchWithUserJoin(businessHistory, typeCondition, condition, pageable);
            default -> fetchWithoutJoin(businessHistory, condition, pageable); // 조인 없이 이력만
        };
    }

    private Page<BusinessHistoryDto> fetchWithoutJoin(QBusinessHistory history, HistorySearchCondition condition, Pageable pageable) {
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue
                ))
                .from(history)
                .where(
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .where(
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private Page<BusinessHistoryDto> fetchWithUserJoin(QBusinessHistory history, BooleanExpression typeCondition, HistorySearchCondition condition, Pageable pageable) {
        QUser user = QUser.user;
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue,
                        user.name
                ))
                .from(history)
                .leftJoin(user).on(history.targetId.eq(user.id))
                .where(
                        nameLike(condition.getName(), user),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(user).on(history.targetId.eq(user.id))
                .where(
                        nameLike(condition.getName(), user),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private Page<BusinessHistoryDto> fetchWithRideJoin(QBusinessHistory history, BooleanExpression typeCondition, HistorySearchCondition condition, Pageable pageable) {
        QSunnyRide qSunnyRide = QSunnyRide.sunnyRide;
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue,
                        qSunnyRide.name
                ))
                .from(history)
                .leftJoin(qSunnyRide).on(history.targetId.eq(qSunnyRide.id))
                .where(
                        nameLike(condition.getName(), qSunnyRide),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(qSunnyRide).on(history.targetId.eq(qSunnyRide.id))
                .where(
                        nameLike(condition.getName(), qSunnyRide),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private Page<BusinessHistoryDto> fetchWithClassJoin(QBusinessHistory history, BooleanExpression typeCondition, HistorySearchCondition condition, Pageable pageable) {
        QSunnyClass qClass = QSunnyClass.sunnyClass;
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue,
                        qClass.name
                ))
                .from(history)
                .leftJoin(qClass).on(history.targetId.eq(qClass.id))
                .where(
                        nameLike(condition.getName(), qClass),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(qClass).on(history.targetId.eq(qClass.id))
                .where(
                        nameLike(condition.getName(), qClass),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private Page<BusinessHistoryDto> fetchWithParentsJoin(QBusinessHistory history, BooleanExpression typeCondition, HistorySearchCondition condition, Pageable pageable) {
        QParents parents = QParents.parents;
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue,
                        parents.name)
                )
                .from(history)
                .leftJoin(parents).on(history.targetId.eq(parents.id))
                .where(
                        nameLike(condition.getName(), parents),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(parents).on(history.targetId.eq(parents.id))
                .where(
                        nameLike(condition.getName(), parents),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private Page<BusinessHistoryDto> fetchWithChild(QBusinessHistory history, BooleanExpression typeCondition, HistorySearchCondition condition, Pageable pageable) {
        QChild child = QChild.child;
        List<BusinessHistoryDto> content = queryFactory
                .select(Projections.constructor(BusinessHistoryDto.class,
                        history.id,
                        history.createdDate,
                        history.targetId,
                        history.targetType,
                        history.action,
                        history.url,
                        history.ip,
                        history.content,
                        history.createdBy,
                        history.newValue,
                        child.name.concat("(").concat(child.className).concat(")")
                ))
                .from(history)
                .leftJoin(child).on(history.targetId.eq(child.id))
                .where(
                        nameLike(condition.getName(), child),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .orderBy(condition.getOrderBy().equals("asc") ? history.id.asc() : history.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(child).on(history.targetId.eq(child.id))
                .where(
                        nameLike(condition.getName(), child),
                        typeCondition,
                        methodEq(condition.getAction()),
                        ipEq(condition.getIp()),
                        createdByEq(condition.getCreatedBy()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression[] makeConditionEq(HistorySearchCondition condition) {
        return new BooleanExpression[]{
                methodEq(condition.getAction()),
                ipEq(condition.getIp()),
                createdByEq(condition.getCreatedBy()),
                createdDateBetween(condition.getStartDate(), condition.getEndDate())};
    }

    private BooleanExpression targetTypeEq(String targetType) {
        return hasText(targetType) ? businessHistory.targetType.eq(targetType) : null;
    }

    private BooleanExpression createdByEq(String createdBy) {
        return hasText(createdBy) ? businessHistory.createdBy.eq(createdBy) : null;
    }
    private BooleanExpression ipEq(String ip) {
        return hasText(ip) ? businessHistory.ip.eq(ip) : null;
    }
    private BooleanExpression methodEq(String method) {
        return hasText(method) ? businessHistory.action.eq(method) : null;
    }
    private BooleanExpression nameLike(String name, QParents child) {
        return hasText(name) ? child.name.contains(name) : null;
    }

    private BooleanExpression nameLike(String name, QSunnyClass child) {
        return hasText(name) ? child.name.contains(name) : null;
    }

    private BooleanExpression nameLike(String name, QSunnyRide child) {
        return hasText(name) ? child.name.contains(name) : null;
    }

    private BooleanExpression nameLike(String name, QUser child) {
        return hasText(name) ? child.name.contains(name) : null;
    }

    private BooleanExpression nameLike(String name, QChild child) {
        return hasText(name) ? child.name.contains(name) : null;
    }

    private BooleanExpression createdDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null && end == null) return null;

        BooleanExpression expression = null;
        if (start != null) {
            expression = businessHistory.createdDate.after(start);
        }
        if (end != null) {
            BooleanExpression endExpr = businessHistory.createdDate.before(end);
            expression = (expression == null) ? endExpr : expression.and(endExpr);
        }
        return expression;
    }

    private boolean hasText(String text) {
        return text != null && !text.isEmpty();
    }

}
