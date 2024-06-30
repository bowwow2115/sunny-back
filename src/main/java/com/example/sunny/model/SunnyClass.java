package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_class")
public class SunnyClass extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Builder
    public SunnyClass(Long id, LocalDateTime createdDate, LocalDateTime modifiedDate, String createdBy, String modifiedBy, String name) {
        super(id, createdDate, modifiedDate, createdBy, modifiedBy);
        this.name = name;
    }
}
