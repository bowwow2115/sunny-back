package com.sunny.model.dto;

import com.sunny.model.ChildMeetingLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ChilMeetingLocationDto {
    //WARNING: ChildDto, SunnyRideDto에서 해당 클래스를 참조할 때 순환참조가 되지 않도록 주의
    private Long id;
    private String comment;
    private ChildDto child;
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
