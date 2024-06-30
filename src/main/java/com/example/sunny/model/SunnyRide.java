package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_ride")
public class SunnyRide extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "is_am", nullable = false)
    private boolean isAm;
    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "sunnyRide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SunnyChildRide> sunnyChildRideList = new ArrayList<>();

    @Builder
    public SunnyRide(Long id, String createdBy, String modifiedBy, String name, boolean isAm, String comment) {
        super(id, createdBy, modifiedBy);
        this.name = name;
        this.isAm = isAm;
        this.comment = comment;
    }
}
