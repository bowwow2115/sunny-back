package com.sunny.model.dto;

import com.sunny.model.BusinessHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

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

    @Size(max = 50, message = "대상 타입은 50자 이하로 입력해야 합니다.")
    private String targetType;      // 대상 타입 (예: Child, MeetingLocation)

    @Size(max = 50, message = "액션은 50자 이하로 입력해야 합니다.")
    private String action;          // 액션

    @Size(max = 500, message = "URL은 500자 이하로 입력해야 합니다.")
    private String url;             // api url

    @Size(max = 15, message = "IP는 15자 이하로 입력해야 합니다.")
    private String ip;              // 사용자 ip

    @Size(max = 1000, message = "상세 내용은 1000자 이하로 입력해야 합니다.")
    private String content;         // 상세 내용

    @Size(max = 50, message = "작업자는 50자 이하로 입력해야 합니다.")
    private String createdBy;         // 작업자

    private Map<String, Object> newValue;   //변경된 값

    @Size(max = 100, message = "이름은 100자 이하로 입력해야 합니다.")
    private String name; // targetType, targetId가 매칭되는 대상의 이름 (예: 원아 이름, 사용자 이름)

    public BusinessHistoryDto(Long id, LocalDateTime createdDate, Long targetId, String targetType, String action, String url, String ip, String content, String createdBy, Map<String, Object> newValue) {
        this.id = id;
        this.createdDate = createdDate;
        this.targetId = targetId;
        this.targetType = targetType;
        this.action = action;
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
        this.action = businessHistory.getAction();
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
                .action(action)
                .url(url)
                .ip(ip)
                .content(content)
                .newValue(newValue)
                .createdBy(createdBy)
                .build();
    }

}
