package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.SunnyChildRide;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.repository.SunnyChildRideRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final SunnyRideRepository sunnyRideRepository;
    private final SunnyChildRideRepository sunnyChildRideRepository;

    @Override
    public ChildDto findByName(String name) {
        return new ChildDto(childRepository.findByname(name));
    }

    @Override
    public List<ChildDto> findAll() {
        return childRepository.findAll().stream()
                .map(ChildDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ChildDto findById(Long aLong) {
        Child child = childRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new ChildDto(child);
    }
    @Transactional
    @Override
    public ChildDto create(ChildDto object) {
        Child child = object.toEntity();
        SunnyChildRide sunnyChildRide = new SunnyChildRide();
        if(object.getAmSunnyRideDto() != null || object.getPmSunnyRideDto() != null) {
            if (object.getAmSunnyRideDto() != null) {
                SunnyRide amRide = sunnyRideRepository.findById(object.getAmSunnyRideDto().getId()).orElseThrow(
                        () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
                sunnyChildRide.setSunnyRide(amRide);
            }
            if (object.getPmSunnyRideDto() != null) {
                SunnyRide pmRide = sunnyRideRepository.findById(object.getAmSunnyRideDto().getId()).orElseThrow(
                        () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
                sunnyChildRide.setSunnyRide(pmRide);
            }
            child.addRide(sunnyChildRide);
            sunnyChildRide.setChild(child);
        }

        return new ChildDto(childRepository.save(child));
    }

    @Override
    public ChildDto update(ChildDto object) {
        return new ChildDto(childRepository.save(object.toEntity()));
    }

    @Override
    public void delete(ChildDto object) {
        childRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        childRepository.deleteById(aLong);
    }
}
