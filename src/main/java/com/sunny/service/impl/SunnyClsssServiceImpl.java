package com.sunny.service.impl;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.SunnyClass;
import com.sunny.model.dto.SunnyClassDto;
import com.sunny.repository.SunnyClassRepository;
import com.sunny.service.SunnyClassServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SunnyClsssServiceImpl implements SunnyClassServcie {

    private final SunnyClassRepository sunnyClassRepository;

    @Override
    public List<SunnyClassDto> findAll() {
        return sunnyClassRepository.findAll().stream()
                .map(SunnyClassDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public SunnyClassDto findById(Long aLong) {
        SunnyClass sunnyClass = sunnyClassRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new SunnyClassDto(sunnyClass);
    }

    @Override
    public SunnyClassDto create(SunnyClassDto object) {
        return new SunnyClassDto(sunnyClassRepository.save(object.toEntity()));
    }

    @Override
    public SunnyClassDto update(SunnyClassDto object) {
        return new SunnyClassDto(sunnyClassRepository.save(object.toEntity()));
    }

    @Override
    public void delete(SunnyClassDto object) {
        sunnyClassRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        sunnyClassRepository.deleteById(aLong);
    }
}
