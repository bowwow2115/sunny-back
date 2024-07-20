package com.example.sunny.model.dto;

import com.example.sunny.model.MeetingLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MeetingLocationDto {
    private Long id;
    private String name;
    private String time;
    private SunnyRideDto sunnyRide;
    private List<ChildRideDto> childRideList = new ArrayList<>();

    public MeetingLocationDto(MeetingLocation meetingLocation) {
        this.id = meetingLocation.getId();
        this.name = meetingLocation.getName();
        this.time = meetingLocation.getTime();
    }

    public MeetingLocation toEntity() {
        return MeetingLocation.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .build();
    }
}
