package com.sunny.service.impl;

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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingLocationServiceImpl implements MeetingLoactionService {

    private final SunnyRideRepository sunnyRideRepository;
    private final MeetingLocationRepository meetingLocationRepository;
    @Override
    @Cacheable(value = "meetingLocations", key = "'all'")
    public List<MeetingLocationDto> findAll() {
        return null;
    }

    @Override
    @Cacheable(value = "meetingLocations", key = "#aLong")
    public MeetingLocationDto findById(Long aLong) {
        return null;
    }

    @Override
    @CacheEvict(value = "meetingLocations", allEntries = true)
    public MeetingLocationDto create(MeetingLocationDto object) {
        MeetingLocation meetingLocation = object.toEntity();
        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "등록하려는 차량의 정보가 존재하지 않습니다."));
        meetingLocation.addSunnyRide(sunnyRide);
        return new MeetingLocationDto(meetingLocationRepository.save(meetingLocation));
    }

    @Override
    @CacheEvict(value = "meetingLocations", allEntries = true)
    public MeetingLocationDto update(MeetingLocationDto object) {
        MeetingLocation origin = meetingLocationRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "수정하려는 승하차장소가 존재하지 않습니다."));
        MeetingLocation meetingLocation = object.toEntity();
        meetingLocation.setChildMeetingLocationList(origin.getChildMeetingLocationList());
        return new MeetingLocationDto(meetingLocationRepository.save(meetingLocation));
    }

    @Override
    @CacheEvict(value = "meetingLocations", allEntries = true)
    public void delete(MeetingLocationDto object) {

    }

    @Override
    @CacheEvict(value = "meetingLocations", allEntries = true)
    public void deleteById(Long aLong) {
        meetingLocationRepository.deleteById(aLong);
    }
}
