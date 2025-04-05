package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.MeetingLocation;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.MeetingLocationDto;
import com.example.sunny.repository.MeetingLoactionRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.MeetingLoactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingLocationServiceImpl implements MeetingLoactionService {

    private final SunnyRideRepository sunnyRideRepository;
    private final MeetingLoactionRepository meetingLoactionRepository;
    @Override
    public List<MeetingLocationDto> findAll() {
        return null;
    }

    @Override
    public MeetingLocationDto findById(Long aLong) {
        return null;
    }

    @Override
    public MeetingLocationDto create(MeetingLocationDto object) {
        MeetingLocation meetingLocation = object.toEntity();
        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "등록하려는 차량의 정보가 존재하지 않습니다."));
        meetingLocation.addSunnyRide(sunnyRide);
        return new MeetingLocationDto(meetingLoactionRepository.save(meetingLocation));
    }

    @Override
    public MeetingLocationDto update(MeetingLocationDto object) {
        MeetingLocation origin = meetingLoactionRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "수정하려는 승하차장소가 존재하지 않습니다."));
        MeetingLocation meetingLocation = object.toEntity();
        meetingLocation.setChildMeetingLocationList(origin.getChildMeetingLocationList());
        return new MeetingLocationDto(meetingLoactionRepository.save(meetingLocation));
    }

    @Override
    public void delete(MeetingLocationDto object) {

    }

    @Override
    public void deleteById(Long aLong) {
        meetingLoactionRepository.deleteById(aLong);
    }
}
