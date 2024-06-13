package com.example.sunny.model;

import com.example.sunny.model.dto.SunnyClassDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_class")
public class SunnyClass extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Builder
    public SunnyClass(Long id, String name) {
        super(id);
        this.name = name;
    }
}
