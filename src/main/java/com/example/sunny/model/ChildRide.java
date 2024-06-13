package com.example.sunny.model;

import com.example.sunny.model.dto.ChildRideDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_ride")
public class ChildRide extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "is_am", nullable = false)
    private boolean isAm;
    @Column(name = "comment")
    private String comment;

    @Builder
    public ChildRide(Long id, String name, boolean isAm, String comment) {
        super(id);
        this.name = name;
        this.isAm = isAm;
        this.comment = comment;
    }
}
