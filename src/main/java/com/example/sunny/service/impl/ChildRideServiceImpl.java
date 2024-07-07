package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.ChildRide;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ChildRideRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.ChildRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildRideServiceImpl implements ChildRideService {
    private final ChildRideRepository childRideRepository;
    private final ChildRepository childRepository;
    private final SunnyRideRepository sunnyRideRepository;

    @Override
    public List<ChildRideDto> findAll() {
        return null;
    }

    @Override
    public ChildRideDto findById(Long aLong) {
        return null;
    }

    @Override
    public ChildRideDto create(ChildRideDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 원아의 정보가 존재하지 않습니다."));

        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 차량의 정보가 존재하지 않습니다."));

        ChildRide childRide = ChildRide.builder()
                .child(child)
                .sunnyRide(sunnyRide)
                .time(object.getTime())
                .comment(object.getComment())
                .build();

        ChildRide result = childRideRepository.save(childRide);

        return ChildRideDto.builder()
                .sunnyRide(new SunnyRideDto(result.getSunnyRide()))
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .time(result.getTime())
                .comment(result.getComment())
                .build();
    }

    @Override
    public ChildRideDto update(ChildRideDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "변경할 원아의 정보가 존재하지 않습니다."));

        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "변경할 차량의 정보가 존재하지 않습니다."));

        ChildRide childRide = ChildRide.builder()
                .id(object.getId())
                .child(child)
                .sunnyRide(sunnyRide)
                .time(object.getTime())
                .comment(object.getComment())
                .build();

        ChildRide result = childRideRepository.save(childRide);

        return ChildRideDto.builder()
                .sunnyRide(new SunnyRideDto(result.getSunnyRide()))
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .time(result.getTime())
                .comment(result.getComment())
                .build();
    }

    @Override
    public void delete(ChildRideDto object) {
    }

    @Override
    public void deleteById(Long aLong) {
        childRideRepository.deleteById(aLong);
    }
}
