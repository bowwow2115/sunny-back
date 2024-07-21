package com.example.sunny.model.dto;

import com.example.sunny.model.ChildRide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildRideDto {
    //WARNING: ChildDto, SunnyRideDto에서 해당 클래스를 참조할 때 순환참조가 되지 않도록 주의
    private Long id;
    private String comment;
    private ChildDto child;
    private MeetingLocationDto meetingLocation;

    public ChildRideDto(ChildRide childRide) {
        this.id = childRide.getId();
        this.comment = childRide.getComment();
    }
}
