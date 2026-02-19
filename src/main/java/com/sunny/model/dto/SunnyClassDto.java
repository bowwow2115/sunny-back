package com.sunny.model.dto;

import com.sunny.model.SunnyClass;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SunnyClassDto {
    private Long id;
    private String name;

    public SunnyClassDto(SunnyClass sunnyClass) {
        this.id = sunnyClass.getId();
        this.name = sunnyClass.getName();
    }

    public SunnyClass toEntity() {
        return SunnyClass.builder()
                .id(id)
                .name(name)
                .build();
    }
}
