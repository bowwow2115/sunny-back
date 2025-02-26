package com.example.sunny.model;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_children_ride")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ChildRide extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_location_id")
    private MeetingLocation meetingLocation;

    @Column(name = "comment")
    @EqualsAndHashCode.Include
    private String comment;

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public void updateChild(Child child) {
        this.child = child;
    }

    public void updateMeetingLocation(MeetingLocation meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    @Builder
    public ChildRide(Long id, String createdBy, String modifiedBy, Child child, MeetingLocation meetingLocation, String comment, String time) {
        super(id, createdBy, modifiedBy);
        this.child = child;
        this.meetingLocation = meetingLocation;
        this.comment = comment;
    }
}
