package com.sunny.service.impl;

import com.sunny.config.aop.TrackHistory;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.SunnyClass;
import com.sunny.model.dto.SunnyClassDto;
import com.sunny.repository.SunnyClassRepository;
import com.sunny.service.SunnyClassServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sunny.code.Action.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SunnyClassServiceImpl implements SunnyClassServcie {

    private final SunnyClassRepository sunnyClassRepository;

    @Override
    @TrackHistory(action = FIND_CLASS_ALL, targetType = SunnyClass.class, noTargetId = true)
    public List<SunnyClassDto> findAll() {
        return sunnyClassRepository.findAll().stream()
                .map(SunnyClassDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @TrackHistory(action = FIND_CLASS_BYID, targetType = SunnyClass.class)
    public SunnyClassDto findById(Long aLong) {
        SunnyClass sunnyClass = sunnyClassRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new SunnyClassDto(sunnyClass);
    }

    @Override
    @TrackHistory(action = CREATE_CLASS, targetType = SunnyClass.class, idParamName = "object")
    @Transactional
    public SunnyClassDto create(SunnyClassDto object) {
        return new SunnyClassDto(sunnyClassRepository.save(object.toEntity()));
    }

    @Override
    @TrackHistory(action = UPDATE_CLASS, targetType = SunnyClass.class, idParamName = "object")
    @Transactional
    public SunnyClassDto update(SunnyClassDto object) {
        return new SunnyClassDto(sunnyClassRepository.save(object.toEntity()));
    }

    @Override
    @TrackHistory(action = DELETE_CLASS, targetType = SunnyClass.class, idParamName = "object")
    @Transactional
    public void delete(SunnyClassDto object) {
        sunnyClassRepository.delete(object.toEntity());
    }

    @Override
    @TrackHistory(action = DELETE_CLASS_BYID, targetType = SunnyClass.class)
    @Transactional
    public void deleteById(Long aLong) {
        sunnyClassRepository.deleteById(aLong);
    }
}
