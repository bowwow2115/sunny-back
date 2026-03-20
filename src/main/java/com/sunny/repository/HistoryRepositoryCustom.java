package com.sunny.repository;

import com.sunny.model.BusinessHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepositoryCustom {
    public void deleteByCreatedDate(LocalDateTime createdDate);

    List<BusinessHistory> findByTargetIdAndType(Long targetId, String targetType);

    long deleteByIds(List<Long> ids);

    List<Long> findOldHistory(LocalDateTime cutoffDate, int batchSize);
}
