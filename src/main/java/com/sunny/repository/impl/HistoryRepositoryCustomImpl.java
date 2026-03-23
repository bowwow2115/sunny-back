package com.sunny.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunny.model.BusinessHistory;
import com.sunny.model.dto.BusinessHistorySearchCondition;
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
    public Page<BusinessHistory> findHistoryByCondition(Pageable pageable,
                                                        BusinessHistorySearchCondition businessHistorySearchCondition) {
        List<BusinessHistory> content = queryFactory
                .selectFrom(businessHistory)
                .orderBy(businessHistory.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(businessHistory.count())
                .from(businessHistory)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
