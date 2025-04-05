package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.ChildMeetingLocation;
import com.example.sunny.model.MeetingLocation;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ChilMeetingLocationDto;
import com.example.sunny.model.dto.MeetingLocationDto;
import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ChildRideRepository;
import com.example.sunny.repository.MeetingLoactionRepository;
import com.example.sunny.service.ChildRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildRideServiceImpl implements ChildRideService {
    private final ChildRideRepository childRideRepository;
    private final ChildRepository childRepository;
//    private final SunnyRideRepository sunnyRideRepository;
    private final MeetingLoactionRepository meetingLoactionRepository;
    @Override
    public List<ChilMeetingLocationDto> findAll() {
        return null;
    }

    @Override
    public ChilMeetingLocationDto findById(Long aLong) {
        return null;
    }

    @Override
    public ChilMeetingLocationDto create(ChilMeetingLocationDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 원아의 정보가 존재하지 않습니다."));

        MeetingLocation meetingLocation = meetingLoactionRepository.findById(object.getMeetingLocation().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 차량의 정보가 존재하지 않습니다."));

        ChildMeetingLocation childMeetingLocation = ChildMeetingLocation.builder()
                .child(child)
                .meetingLocation(meetingLocation)
                .comment(object.getComment())
                .build();

        ChildMeetingLocation result = childRideRepository.save(childMeetingLocation);

        //MeetingLoaction 하위 SunnyRide 정보 추가 매핑
        MeetingLocationDto meetingLocationDto = new MeetingLocationDto(result.getMeetingLocation());
        SunnyRideDto sunnyRideDto = new SunnyRideDto(result.getMeetingLocation().getSunnyRide());
        meetingLocationDto.setSunnyRide( sunnyRideDto);


        return ChilMeetingLocationDto.builder()
                .meetingLocation(meetingLocationDto)
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .comment(result.getComment())
                .build();
    }

    @Override
    public ChilMeetingLocationDto update(ChilMeetingLocationDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "변경할 원아의 정보가 존재하지 않습니다."));

        MeetingLocation meetingLocation = meetingLoactionRepository.findById(object.getMeetingLocation().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 차량의 정보가 존재하지 않습니다."));

        ChildMeetingLocation childMeetingLocation = ChildMeetingLocation.builder()
                .id(object.getId())
                .child(child)
                .meetingLocation(meetingLocation)
                .comment(object.getComment())
                .build();

        ChildMeetingLocation result = childRideRepository.save(childMeetingLocation);

        //MeetingLoaction 하위 SunnyRide 정보 추가 매핑
        MeetingLocationDto meetingLocationDto = new MeetingLocationDto(result.getMeetingLocation());
        SunnyRideDto sunnyRideDto = new SunnyRideDto(result.getMeetingLocation().getSunnyRide());
        meetingLocationDto.setSunnyRide( sunnyRideDto);

        return ChilMeetingLocationDto.builder()
                .meetingLocation(meetingLocationDto)
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .comment(result.getComment())
                .build();
    }

    @Override
    public void delete(ChilMeetingLocationDto object) {
    }

    @Override
    public void deleteById(Long aLong) {
        childRideRepository.deleteById(aLong);
    }
}
