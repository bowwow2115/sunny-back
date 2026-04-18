package com.sunny.model.dto;

import com.sunny.model.ChildMeetingLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChilMeetingLocationDto {
    //WARNING: ChildDto, SunnyRideDto에서 해당 클래스를 참조할 때 순환참조가 되지 않도록 주의
    private Long id;

    @Size(max = 500, message = "코멘트는 500자 이하로 입력해야 합니다.")
    private String comment;

    @Valid
    private ChildDto child;

    @Valid
    private MeetingLocationDto meetingLocation;

    public ChilMeetingLocationDto(ChildMeetingLocation childMeetingLocation) {
        this.id = childMeetingLocation.getId();
        this.comment = childMeetingLocation.getComment();
    }

    public ChildMeetingLocation toEntity() {
        return ChildMeetingLocation.builder()
                .id(id)
                .comment(comment)
                .build();
    }
}
