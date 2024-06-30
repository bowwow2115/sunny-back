package com.example.sunny.model.embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class SunnyRideDto {
    private Long id;
    private String name;
    private String time;
    private String comment;
}
