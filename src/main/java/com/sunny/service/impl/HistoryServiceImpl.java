package com.sunny.service.impl;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.BusinessHistory;
import com.sunny.repository.HistoryRepository;
import com.sunny.service.HistoryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final EntityManager entityManager;

    @Override
    @Async
    public void asyncCreate(BusinessHistory object) {
        historyRepository.save(object);
    }

    @Override
    public List<BusinessHistory> findByTargetIdAndType(Long targetId, String targetType) {
        return historyRepository.findByTargetIdAndType(targetId, targetType);
    }

    @Override
    @Transactional
    public long deleteByCreatedDateBatch(LocalDateTime cutoffDate, int batchSize) {
        // 메모리 누수 방지
        entityManager.clear();

        List<Long> oldIds = historyRepository.findOldHistory(cutoffDate, batchSize);
        if (oldIds.isEmpty()) {
            return 0;
        }
        return historyRepository.deleteByIds(oldIds);
    }

    @Override
    public List<BusinessHistory> findAll() {
        return historyRepository.findAll();
    }

    @Override
    public BusinessHistory findById(Long aLong) {
        return historyRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    public BusinessHistory create(BusinessHistory object) {
        return historyRepository.save(object);
    }

    @Override
    public BusinessHistory update(BusinessHistory object) {
        return historyRepository.save(object);
    }

    @Override
    public void delete(BusinessHistory object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
