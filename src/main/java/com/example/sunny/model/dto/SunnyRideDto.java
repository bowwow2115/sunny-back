package com.example.sunny.model.dto;

import com.example.sunny.model.SunnyRide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SunnyRideDto {
    private Long id;
    private String name;
    private boolean isAm;
    private String comment;
    private String time;
    private List<MeetingLocationDto> meetingLocationList;
    public SunnyRideDto(SunnyRide childRide) {
        this.id = childRide.getId();
        this.name = childRide.getName();
        this.isAm = childRide.isAm();
        this.comment = childRide.getComment();
        this.time = childRide.getTime();
    }

    public SunnyRide toEntity() {
        return SunnyRide.builder()
                .id(id)
                .name(name)
                .isAm(isAm)
                .comment(comment)
                .time(time)
                .build();
    }

}
