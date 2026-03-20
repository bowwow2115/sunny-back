package com.sunny.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunny.model.BusinessHistory;
import com.sunny.repository.HistoryRepositoryCustom;
import lombok.RequiredArgsConstructor;
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
}
