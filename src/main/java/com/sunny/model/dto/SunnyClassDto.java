package com.sunny.model.dto;

import com.sunny.model.SunnyClass;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SunnyClassDto {
    private Long id;

    @NotBlank(message = "반 이름은 필수입니다.")
    @Size(min = 1, max = 50, message = "반 이름은 1자 이상 50자 이하로 입력해야 합니다.")
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
