package com.example.sunny.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "sunny_ride")
public class ChildRide extends BaseEntity {
    private boolean isAm;
    private String name;
    private String comment;
}
