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
        //등록된 차량인지 확인
        Child child = object.toEntity();
        SunnyRide amRide = null;
        SunnyRide pmRide = null;
        if (object.getAmRide() != null) {
            amRide = sunnyRideRepository.findById(object.getAmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            SunnyChildRide sunnyChildRide = new SunnyChildRide();
            sunnyChildRide.setSunnyRide(amRide);
            sunnyChildRide.setTime(object.getAmRide().getTime());
            sunnyChildRide.setComment(object.getAmRide().getComment());
            child.addRide(sunnyChildRide);
        }
        if (object.getPmRide() != null) {
            pmRide = sunnyRideRepository.findById(object.getPmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            SunnyChildRide sunnyChildRide = new SunnyChildRide();
            sunnyChildRide.setSunnyRide(pmRide);
//            sunnyChildRide.setChild(child);
            sunnyChildRide.setTime(object.getPmRide().getTime());
            sunnyChildRide.setComment(object.getPmRide().getComment());
            child.addRide(sunnyChildRide);
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
