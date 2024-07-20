package com.example.sunny.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sunny_meeting_location")
public class MeetingLocation extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", updatable = false)
    @Setter
    private SunnyRide sunnyRide;

    @OneToMany(mappedBy = "meetingLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildRide> childRideList = new ArrayList<>();

    @Builder
    public MeetingLocation(Long id, String createdBy, String modifiedBy, String name, String time, SunnyRide sunnyRide) {
        super(id, createdBy, modifiedBy);
        this.name = name;
        this.time = time;
        this.sunnyRide = sunnyRide;
    }

    public void addSunnyRide(SunnyRide sunnyRide) {
        this.sunnyRide = sunnyRide;
        sunnyRide.addMeetingLocation(this);
    }
}
