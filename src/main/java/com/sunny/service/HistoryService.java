package com.sunny.service;

import com.sunny.model.BusinessHistory;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService extends CrudService<BusinessHistory, Long> {
    @Async
    public void asyncCreate(BusinessHistory object);

    List<BusinessHistory> findByTargetIdAndType(Long targetId, String targetType);

    long deleteByCreatedDateBatch(LocalDateTime cutoffDate, int batchSize);
}
