package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.SunnyRideDto;
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
                .map(SunnyRideDto::new)
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
}
