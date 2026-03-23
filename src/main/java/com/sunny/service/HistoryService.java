package com.sunny.service;

import com.sunny.model.BusinessHistory;
import com.sunny.model.dto.BusinessHistoryDto;
import com.sunny.model.dto.BusinessHistorySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService extends CrudService<BusinessHistoryDto, Long> {
    @Async
    public void asyncCreate(BusinessHistory object);

    List<BusinessHistoryDto> findByTargetIdAndType(Long targetId, String targetType);

    long deleteByCreatedDateBatch(LocalDateTime cutoffDate, int batchSize);

    Page<BusinessHistoryDto> getHistoryByCondition(Pageable pageable, BusinessHistorySearchCondition businessHistorySearchCondition);
}
