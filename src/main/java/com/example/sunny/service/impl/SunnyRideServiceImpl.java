package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.*;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.SunnyRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SunnyRideServiceImpl implements SunnyRideService {
    private final SunnyRideRepository childRideRepository;

    @Override
    public List<SunnyRideDto> findAll() {
        return childRideRepository.findAll().stream()
                .map((result)-> {
                    SunnyRideDto sunnyRideDto = new SunnyRideDto(result);
                    return addJoinData(sunnyRideDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    public SunnyRideDto findById(Long aLong) {
        SunnyRide childRide = childRideRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new SunnyRideDto(childRide);
    }

    @Override
    public SunnyRideDto create(SunnyRideDto object) {
        return new SunnyRideDto(childRideRepository.save(object.toEntity()));
    }

    @Override
    public SunnyRideDto update(SunnyRideDto object) {
        return new SunnyRideDto(childRideRepository.save(object.toEntity()));
    }

    @Override
    public void delete(SunnyRideDto object) {
        childRideRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        childRideRepository.deleteById(aLong);
    }

    private SunnyRideDto addJoinData(SunnyRideDto sunnyRideDto, SunnyRide result) {

        if (result.getMeetingLocationList() != null && result.getMeetingLocationList().size() != 0) {
            List<MeetingLocationDto> meetingLocationDtoList = result.getMeetingLocationList().stream()
                    .map((item) -> {
                        MeetingLocationDto meetingLocationDto = new MeetingLocationDto(item);
                        //MeetingLocaiotn(집결지)에 원아리스트 매핑
                        List<ChildRideDto> childRideDtoList = item.getChildRideList().stream()
                                .map((childRide) -> {
                                    ChildRideDto childRideDto = new ChildRideDto(childRide);
                                    ChildDto childDto = new ChildDto(childRide.getChild());
                                    //Child에 ParentsList 매핑
                                    childDto.setParentList(childRide.getChild().getParentList().stream()
                                            .map((parents -> new ParentsDto(parents)))
                                            .collect(Collectors.toList()));
                                    childRideDto.setChild(childDto);
                                    return childRideDto;
                                })
                                .collect(Collectors.toList());
                        meetingLocationDto.setChildRideList(childRideDtoList);
                        return meetingLocationDto;
                    })
                    .sorted(Comparator.comparing(MeetingLocationDto::getTime))
                    .collect(Collectors.toList());
            sunnyRideDto.setMeetingLocationList(meetingLocationDtoList);
        }
        return sunnyRideDto;
    }

}
