package com.sunny.model.dto;

import com.sunny.model.BusinessHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHistoryDto {
    private Long id;
    private LocalDateTime createdDate;
    private Long targetId;        // 대상 ID (예: 원아, 사용자 id)
    private String targetType;      // 대상 타입 (예: Child, MeetingLocation)
    private String method;          // 액션
    private String url;             // api url
    private String ip;              // 사용자 ip
    private String content;         // 상세 내용
    private String createdBy;         // 작업자
    private Map<String, Object> newValue;   //변경된 값
    private String name; // targetType, targetId가 매칭되는 대상의 이름 (예: 원아 이름, 사용자 이름)

    public BusinessHistoryDto(Long id, LocalDateTime createdDate, Long targetId, String targetType, String method, String url, String ip, String content, String createdBy, Map<String, Object> newValue) {
        this.id = id;
        this.createdDate = createdDate;
        this.targetId = targetId;
        this.targetType = targetType;
        this.method = method;
        this.url = url;
        this.ip = ip;
        this.content = content;
        this.createdBy = createdBy;
        this.newValue = newValue;
    }

    public BusinessHistoryDto(BusinessHistory businessHistory) {
        this.id = businessHistory.getId();
        this.createdDate = businessHistory.getCreatedDate();
        this.targetId = businessHistory.getTargetId();
        this.targetType = businessHistory.getTargetType();
        this.method = businessHistory.getMethod();
        this.url = businessHistory.getUrl();
        this.ip = businessHistory.getIp();
        this.createdBy = businessHistory.getCreatedBy();
        this.content = businessHistory.getContent();
    }

    public BusinessHistory toEntity() {
        return BusinessHistory.builder()
                .id(id)
                .createdDate(createdDate)
                .targetId(targetId)
                .targetType(targetType)
                .method(method)
                .url(url)
                .ip(ip)
                .content(content)
                .newValue(newValue)
                .createdBy(createdBy)
                .build();
    }

}
