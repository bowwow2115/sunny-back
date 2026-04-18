package com.sunny.model.dto;

import com.sunny.model.MeetingLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MeetingLocationDto {
    private Long id;

    @NotBlank(message = "만남 위치 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "만남 위치 이름은 1자 이상 100자 이하로 입력해야 합니다.")
    private String name;

    @NotBlank(message = "시간은 필수입니다.")
    @Size(min = 1, max = 20, message = "시간은 1자 이상 20자 이하로 입력해야 합니다.")
    private String time;

    @Size(max = 500, message = "코멘트는 500자 이하로 입력해야 합니다.")
    private String comment;

    @Valid
    private SunnyRideDto sunnyRide;

    @Valid
    @Default
    private List<ChilMeetingLocationDto> childRideList = new ArrayList<>();

    public MeetingLocationDto(MeetingLocation meetingLocation) {
        this.id = meetingLocation.getId();
        this.name = meetingLocation.getName();
        this.time = meetingLocation.getTime();
        this.comment = meetingLocation.getComment();
    }

    public MeetingLocation toEntity() {
        return MeetingLocation.builder()
                .id(this.id)
                .name(this.name)
                .time(this.time)
                .comment(this.comment)
                .build();
    }
}
