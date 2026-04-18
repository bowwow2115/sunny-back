package com.sunny.model.dto;

import com.sunny.model.SunnyRide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SunnyRideDto {
    private Long id;

    @NotBlank(message = "탑승 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "탑승 이름은 1자 이상 100자 이하로 입력해야 합니다.")
    private String name;

    private boolean isAm;

    @Size(max = 500, message = "코멘트는 500자 이하로 입력해야 합니다.")
    private String comment;

    @NotBlank(message = "시간은 필수입니다.")
    @Size(min = 1, max = 20, message = "시간은 1자 이상 20자 이하로 입력해야 합니다.")
    private String time;

    @Valid
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
