package com.sunny.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BusinessHistorySearchCondition {
    private String name; // 원아 혹은
    private List<Long> targetIdList;
    private List<String> targetTypeList;
    private String method; // POST, GET, PUT, DELETE
    private String ip;
    private String orderBy;
    private String createdBy; // 작업자
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
