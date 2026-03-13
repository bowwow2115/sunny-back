package com.sunny.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionUtil {

    /**
     * 트랜잭션 동기화 등록 (안전 장치 포함)
     */
    public void registerSynchronizationSafe(TransactionSynchronization sync) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(sync);
        } else {
            // 트랜잭션이 없으면 바로 실행 (또는 로깅)
            sync.afterCommit();
        }
    }

    /**
     * 현재 트랜잭션 활성화 여부
     */
    public boolean isTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}