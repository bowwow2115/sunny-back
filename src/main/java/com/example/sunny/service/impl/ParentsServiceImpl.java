package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Parents;
import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentsServiceImpl implements ParentsService {
    private final ParentsRepository parentsRepository;

    @Override
    public List<ParentsDto> findAll() {
        return parentsRepository.findAll().stream()
                .map(ParentsDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ParentsDto findById(Long aLong) {
        Parents parents = parentsRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new ParentsDto(parents);
    }

    @Override
    public ParentsDto create(ParentsDto object) {
        return new ParentsDto(object.toEntity());
    }

    @Override
    public ParentsDto update(ParentsDto object) {
        return new ParentsDto(object.toEntity());
    }

    @Override
    public void delete(ParentsDto object) {
        parentsRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        parentsRepository.deleteById(aLong);
    }

    @Override
    public ParentsDto findByName(String name) {
        return new ParentsDto(parentsRepository.findByName(name));
    }
}
