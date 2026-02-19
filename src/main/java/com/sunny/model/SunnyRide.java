package com.sunny.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_ride")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class SunnyRide extends BaseEntity {
    @Column(name = "name", nullable = false)
    @EqualsAndHashCode.Include
    private String name;
    @Column(name = "is_am", nullable = false)
    @EqualsAndHashCode.Include
    private boolean isAm;
    @Column(name = "comment")
    @EqualsAndHashCode.Include
    private String comment;

    @Column(name = "time")
    @EqualsAndHashCode.Include
    private String time;

    @OneToMany(mappedBy = "sunnyRide", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Setter
    private List<MeetingLocation> meetingLocationList = new ArrayList<>();

    @Builder
    public SunnyRide(Long id, String createdBy, String modifiedBy, String name, boolean isAm, String comment, String time) {
        super(id, createdBy, modifiedBy);
        this.name = name;
        this.isAm = isAm;
        this.comment = comment;
        this.time = time;
    }

    public void addMeetingLocation(MeetingLocation meetingLocation) {
        this.meetingLocationList.add(meetingLocation);
        meetingLocation.updateSunnyRide(this);
    }

    public void removeMeetingLoaction(MeetingLocation meetingLocation) {
        this.meetingLocationList.remove(meetingLocation);
        meetingLocation.updateSunnyRide(null);
    }
}
