package com.example.sunny.model.dto;

import com.example.sunny.model.SunnyRide;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SunnyRideDto {
    private Long id;
    private String name;
    private boolean isAm;
    private String comment;

    public SunnyRideDto(SunnyRide childRide) {
        this.id = childRide.getId();
        this.name = childRide.getName();
        this.isAm = childRide.isAm();
        this.comment = childRide.getComment();
    }

    public SunnyRide toEntity() {
        return SunnyRide.builder()
                .id(id)
                .name(name)
                .isAm(isAm)
                .comment(comment)
                .build();
    }

}
