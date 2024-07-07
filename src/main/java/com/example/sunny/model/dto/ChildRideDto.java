package com.example.sunny.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildRideDto {
    //WARNING: ChildDto, SunnyRideDto에서 해당 클래스를 참조할 때 순환참조가 되지 않도록 주의
    private Long id;
    private String comment;
    private String time;
    private ChildDto child;
    private SunnyRideDto sunnyRide;
}
