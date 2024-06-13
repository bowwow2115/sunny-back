package com.example.sunny.model.dto;

import com.example.sunny.model.ChildRide;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChildRideDto {
    private Long id;
    private String name;
    private boolean isAm;
    private String comment;

    public ChildRideDto(ChildRide childRide) {
        this.id = childRide.getId();
        this.name = childRide.getName();
        this.isAm = childRide.isAm();
        this.comment = childRide.getComment();
    }

    public ChildRide toEntity() {
        return ChildRide.builder()
                .id(id)
                .name(name)
                .isAm(isAm)
                .comment(comment)
                .build();
    }

}
