package com.sunny.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistorySearchCondition {
    private String name; // targetType의 targetId와 매칭되는 대상의 이름 (예: 원아 이름, 사용자 이름)
    private Long targetId;
    private String targetType;
    private String method; // POST, GET, PUT, DELETE
    private String ip;
    private String orderBy; // id 정렬기준 asc, desc
    private String createdBy; // 작업자
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
