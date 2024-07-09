package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.Parents;
import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ParentsDto findByName(String name) {
        return new ParentsDto(parentsRepository.findByName(name));
    }
}
