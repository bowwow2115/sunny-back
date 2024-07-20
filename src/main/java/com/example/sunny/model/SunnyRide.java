package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_ride")
public class SunnyRide extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "is_am", nullable = false)
    private boolean isAm;
    @Column(name = "comment")
    private String comment;

    @Column(name = "time")
    private String time;

    @OneToMany(mappedBy = "sunnyRide", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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
        meetingLocation.setSunnyRide(this);
    }

    public void removeMeetingLoaction(MeetingLocation meetingLocation) {
        this.meetingLocationList.remove(meetingLocation);
        meetingLocation.setSunnyRide(null);
    }
}
