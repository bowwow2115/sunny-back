package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.*;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.SunnyRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (result.getChildRideList() != null && result.getChildRideList().size() != 0) {
            List<ChildRideDto> childRideDtoList = result.getChildRideList().stream()
                    .map((item) -> {
                            ChildDto childDto = new ChildDto(item.getChild());
                            childDto.setParentList(
                                    item.getChild().getParentList().stream().map( (parents) ->
                                            new ParentsDto(parents)
                                    ).collect(Collectors.toList()));
                             return ChildRideDto.builder()
                                        .id(item.getId())
                                        .comment(item.getComment())
                                        .meetingLocation(new MeetingLocationDto(item.getMeetingLocation()))
                                        .child(childDto)
                                        .build();
                            }
                    )
//                    .sorted(Comparator.comparing(ChildRideDto::getMeetingLocation))
                    .collect(Collectors.toList());
            sunnyRideDto.setSunnyChildRideList(childRideDtoList);
        }
        return sunnyRideDto;
    }

}
