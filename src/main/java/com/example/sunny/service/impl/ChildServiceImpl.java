package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.service.ChildService;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ParentsService parentsService;

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
//        List<ParentsDto> parentsDtoList = new ArrayList<>();
//        if(object.getParentList() != null && object.getParentList().size() != 0) {
//            for(ParentsDto parentsDto : object.getParentList()) {
//                ParentsDto savedParentsDto = parentsService.create(parentsDto);
//                parentsDtoList.add(savedParentsDto);
//            }
//        }
//        object.setParentList(parentsDtoList);
        return new ChildDto(childRepository.save(object.toEntity()));
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
