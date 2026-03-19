package com.sunny.service.impl;

import com.sunny.config.aop.TrackHistory;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.MeetingLocation;
import com.sunny.model.SunnyRide;
import com.sunny.model.dto.MeetingLocationDto;
import com.sunny.repository.MeetingLocationRepository;
import com.sunny.repository.SunnyRideRepository;
import com.sunny.service.MeetingLoactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sunny.code.Action.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingLocationServiceImpl implements MeetingLoactionService {

    private final SunnyRideRepository sunnyRideRepository;
    private final MeetingLocationRepository meetingLocationRepository;
    @Override
    public List<MeetingLocationDto> findAll() {
        return null;
    }

    @Override
    public MeetingLocationDto findById(Long aLong) {
        return null;
    }

    @Override
    @TrackHistory(action = CREATE_MEETINGLOCATION, targetType = MeetingLocation.class, idParamName = "object")
    public MeetingLocationDto create(MeetingLocationDto object) {
        MeetingLocation meetingLocation = object.toEntity();
        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "등록하려는 차량의 정보가 존재하지 않습니다."));
        meetingLocation.addSunnyRide(sunnyRide);
        return new MeetingLocationDto(meetingLocationRepository.save(meetingLocation));
    }

    @Override
    @TrackHistory(action = UPDATE_MEETINGLOCATION, targetType = MeetingLocation.class, idParamName = "object")
    public MeetingLocationDto update(MeetingLocationDto object) {
        MeetingLocation origin = meetingLocationRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "수정하려는 승하차장소가 존재하지 않습니다."));
        MeetingLocation meetingLocation = object.toEntity();
        meetingLocation.setChildMeetingLocationList(origin.getChildMeetingLocationList());
        return new MeetingLocationDto(meetingLocationRepository.save(meetingLocation));
    }

    @Override
    public void delete(MeetingLocationDto object) {

    }

    @Override
    @TrackHistory(action = DELETE_MEETINGLOCATION_BYID, targetType = MeetingLocation.class)
    public void deleteById(Long aLong) {
        meetingLocationRepository.deleteById(aLong);
    }
}
