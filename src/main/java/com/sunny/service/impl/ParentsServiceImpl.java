package com.sunny.service.impl;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.Child;
import com.sunny.model.Parents;
import com.sunny.model.dto.ParentsDto;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.ParentsRepository;
import com.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentsServiceImpl implements ParentsService {
    private final ParentsRepository parentsRepository;
    private final ChildRepository childRepository;
    @Override
    public List<ParentsDto> findAll() {
        return null;
//        return parentsRepository.findAll().stream()
//                .map(ParentsDto::new)
//                .collect(Collectors.toList());
    }

    @Override
    public ParentsDto findById(Long aLong) {
        Parents parents = parentsRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new ParentsDto(parents);
    }

    @Override
    public ParentsDto create(ParentsDto object) {
        Parents parents = object.toEntity();
        Child child = childRepository.findById(object.getChildId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "등록하려는 원아의 정보가 존재하지 않습니다."));
        parents.addChild(child);
        return new ParentsDto(parentsRepository.save(parents));
    }

    @Override
    public ParentsDto update(ParentsDto object) {
        return new ParentsDto(parentsRepository.save(object.toEntity()));
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
    public List<ParentsDto> findByName(String name) {
        List<Parents> parentsList = parentsRepository.findByName(name);
        if(parentsList == null) parentsList = new ArrayList<>();
        return parentsList.stream().map(ParentsDto::new).collect(Collectors.toList());
    }
}
