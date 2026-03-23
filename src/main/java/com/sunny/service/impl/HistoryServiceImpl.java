package com.sunny.service.impl;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.BusinessHistory;
import com.sunny.model.dto.BusinessHistoryDto;
import com.sunny.model.dto.BusinessHistorySearchCondition;
import com.sunny.repository.HistoryRepository;
import com.sunny.service.HistoryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<BusinessHistoryDto> findByTargetIdAndType(Long targetId, String targetType) {
        return historyRepository.findByTargetIdAndType(targetId, targetType).stream()
                .map(BusinessHistoryDto::new)
                .collect(Collectors.toList());
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
    @Transactional
    public Page<BusinessHistoryDto> getHistoryByCondition(Pageable pageable, BusinessHistorySearchCondition businessHistorySearchCondition) {
        return historyRepository.findHistoryByCondition(pageable, businessHistorySearchCondition).map(BusinessHistoryDto::new);
    }

    @Override
    public List<BusinessHistoryDto> findAll() {
        return null;
    }

    @Override
    public BusinessHistoryDto findById(Long aLong) {
        return new BusinessHistoryDto(historyRepository.findById(aLong)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND)));
    }

    @Override
    public BusinessHistoryDto create(BusinessHistoryDto object) {
        return new BusinessHistoryDto(historyRepository.save(object.toEntity()));
    }

    @Override
    public BusinessHistoryDto update(BusinessHistoryDto object) {
        return null;
    }

    @Override
    public void delete(BusinessHistoryDto object) {
    }

    @Override
    public void deleteById(Long aLong) {
    }
}
