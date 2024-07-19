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
public class ChildRide extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    @Setter
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_location_id")
    @Setter
    private MeetingLocation meetingLocation;

    @Column(name = "comment")
    @Setter
    private String comment;

    @Builder
    public ChildRide(Long id, String createdBy, String modifiedBy, Child child, MeetingLocation meetingLocation, String comment, String time) {
        super(id, createdBy, modifiedBy);
        this.child = child;
        this.meetingLocation = meetingLocation;
        this.comment = comment;
    }
}
