package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.ChildRide;
import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.repository.ChildRideRepository;
import com.example.sunny.service.ChildRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildRideServiceImpl implements ChildRideService {
    private final ChildRideRepository childRideRepository;

    @Override
    public List<ChildRideDto> findAll() {
        return childRideRepository.findAll().stream()
                .map(ChildRideDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ChildRideDto findById(Long aLong) {
        ChildRide childRide = childRideRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new ChildRideDto(childRide);
    }

    @Override
    public ChildRideDto create(ChildRideDto object) {
        return new ChildRideDto(childRideRepository.save(object.toEntity()));
    }

    @Override
    public ChildRideDto update(ChildRideDto object) {
        return new ChildRideDto(childRideRepository.save(object.toEntity()));
    }

    @Override
    public void delete(ChildRideDto object) {
        childRideRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        childRideRepository.deleteById(aLong);
    }
}
