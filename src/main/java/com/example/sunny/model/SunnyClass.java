package com.example.sunny.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sunny_class")
public class SunnyClass extends BaseEntity{
    @Column(name = "name")
    private String name;
}
