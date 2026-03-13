package com.sunny.service.impl;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.BusinessHistory;
import com.sunny.repository.HistoryRepository;
import com.sunny.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    @Override
    public List<BusinessHistory> findAll() {
        return historyRepository.findAll();
    }

    @Override
    public BusinessHistory findById(Long aLong) {
        return historyRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
//    @Transactional
    @Async
    public BusinessHistory create(BusinessHistory object) {

//        TransactionSynchronizationManager.registerSynchronization(
//                new TransactionSynchronization() {
//                    @Override
//                    public void afterCommit() {
//                        // 메인 트랜잭션 커밋 후에만 실행
//                        historyRepository.save(object);
//                    }
//                }
//        );
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

    @Override
    @Async
    public void save(BusinessHistory businessHistory) {
        historyRepository.save(businessHistory);
    }
}
