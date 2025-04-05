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
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MeetingLocation extends BaseEntity{
    @Column(name = "name")
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "time")
    @EqualsAndHashCode.Include
    private String time;

    @Column(name = "comment")
    @EqualsAndHashCode.Include
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", updatable = false)
    private SunnyRide sunnyRide;

    @OneToMany(mappedBy = "meetingLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<ChildMeetingLocation> childMeetingLocationList = new ArrayList<>();

    @Builder
    public MeetingLocation(Long id, String createdBy, String modifiedBy, String name, String time, String comment, SunnyRide sunnyRide) {
        super(id, createdBy, modifiedBy);
        this.name = name;
        this.time = time;
        this.comment = comment;
        this.sunnyRide = sunnyRide;
    }

    public void addSunnyRide(SunnyRide sunnyRide) {
        this.sunnyRide = sunnyRide;
        sunnyRide.addMeetingLocation(this);
    }

    public void updateSunnyRide(SunnyRide sunnyRide) {
        this.sunnyRide = sunnyRide;
    }
}
