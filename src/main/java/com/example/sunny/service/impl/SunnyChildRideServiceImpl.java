package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.SunnyChildRide;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.SunnyChildRideDto;
import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.SunnyChildRideRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.SunnyChildRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SunnyChildRideServiceImpl implements SunnyChildRideService {
    private final SunnyChildRideRepository sunnyChildRideRepository;
    private final ChildRepository childRepository;
    private final SunnyRideRepository sunnyRideRepository;

    @Override
    public List<SunnyChildRideDto> findAll() {
        return null;
    }

    @Override
    public SunnyChildRideDto findById(Long aLong) {
        return null;
    }

    @Override
    public SunnyChildRideDto create(SunnyChildRideDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 원아의 정보가 존재하지 않습니다."));

        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "생성할 차량의 정보가 존재하지 않습니다."));

        SunnyChildRide childRide = SunnyChildRide.builder()
                .child(child)
                .sunnyRide(sunnyRide)
                .time(object.getTime())
                .comment(object.getComment())
                .build();

        SunnyChildRide result = sunnyChildRideRepository.save(childRide);

        return SunnyChildRideDto.builder()
                .sunnyRide(new SunnyRideDto(result.getSunnyRide()))
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .time(result.getTime())
                .comment(result.getComment())
                .build();
    }

    @Override
    public SunnyChildRideDto update(SunnyChildRideDto object) {
        Child child = childRepository.findById(object.getChild().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "변경할 원아의 정보가 존재하지 않습니다."));

        SunnyRide sunnyRide = sunnyRideRepository.findById(object.getSunnyRide().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "변경할 차량의 정보가 존재하지 않습니다."));

        SunnyChildRide childRide = SunnyChildRide.builder()
                .id(object.getId())
                .child(child)
                .sunnyRide(sunnyRide)
                .time(object.getTime())
                .comment(object.getComment())
                .build();

        SunnyChildRide result = sunnyChildRideRepository.save(childRide);

        return SunnyChildRideDto.builder()
                .sunnyRide(new SunnyRideDto(result.getSunnyRide()))
                .child(new ChildDto(result.getChild()))
                .id(result.getId())
                .time(result.getTime())
                .comment(result.getComment())
                .build();
    }

    @Override
    public void delete(SunnyChildRideDto object) {
    }

    @Override
    public void deleteById(Long aLong) {
        sunnyChildRideRepository.deleteById(aLong);
    }
}
