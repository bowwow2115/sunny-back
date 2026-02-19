package com.sunny.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_class")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SunnyClass extends BaseEntity{
    @Column(name = "name")
    @EqualsAndHashCode.Include
    private String name;

    @Builder
    public SunnyClass(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String createdBy, String modifiedBy, String name) {
        super(id, createdDate, modifiedDate, createdBy, modifiedBy);
        this.name = name;
    }
}
