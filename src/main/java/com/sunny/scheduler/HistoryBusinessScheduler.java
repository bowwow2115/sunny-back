package com.sunny.scheduler;

import com.sunny.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        name = "scheduler.history.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class HistoryBusinessScheduler {
    private final HistoryService historyService;
    @Value("${scheduler.history.batch-size:1000}")
    private int batchSize;
    @Value("${scheduler.history.retention-period-years:1}")
    private int retentionPeriodYears;

    @Scheduled(cron = "${scheduler.history.cron:0 0 2 * * *}")
    public void deleteByCreatedDate() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusYears(retentionPeriodYears);
        long deletedCount;
        log.info("HistoryBusinessScheduler 실행");
        do {
            deletedCount = historyService.deleteByCreatedDateBatch(cutoffDate, batchSize);

            log.info("배치 삭제 진행 중... {} 건", deletedCount);
        } while (deletedCount > 0);
    }
}