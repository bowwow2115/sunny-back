package com.sunny.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sunny_business_history")
public class BusinessHistory extends BaseEntity {
    private Long targetId;        // 대상 ID (예: 원아, 사용자 id)
    private String targetType;      // 대상 타입 (예: Child, MeetingLocation)
    private String method;          // 액션
    private String url;             // api url
    private String ip;              // 사용자 ip
    private String content;         // 상세 내용
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> newValue;   //변경된 값

    @Builder
    public BusinessHistory(Long id, String createdBy, String modifiedBy, Long targetId, String targetType, String method, String url, String ip, String content, Map<String, Object> newValue) {
        super(id, createdBy, modifiedBy);
        this.targetId = targetId;
        this.targetType = targetType;
        this.method = method;
        this.url = url;
        this.ip = ip;
        this.content = content;
        this.newValue = newValue;
    }
}