package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children_ride")
public class SunnyChildRide extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @Setter
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id")
    @Setter
    private SunnyRide sunnyRide;

    @Column(name = "comment")
    @Setter
    private String comment;

    @Column(name = "time")
    @Setter
    private String time;

    @Builder
    public SunnyChildRide(Long id, String createdBy, String modifiedBy, Child child, SunnyRide sunnyRide, String comment, String time) {
        super(id, createdBy, modifiedBy);
        this.child = child;
        this.sunnyRide = sunnyRide;
        this.comment = comment;
        this.time = time;
    }
}
