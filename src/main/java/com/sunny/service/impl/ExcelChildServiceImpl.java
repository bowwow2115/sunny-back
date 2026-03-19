package com.sunny.service.impl;

import com.sunny.code.Action;
import com.sunny.config.aop.TrackHistory;
import com.sunny.model.Child;
import com.sunny.model.dto.ChildDto;
import com.sunny.service.ChildService;
import com.sunny.service.ExcelChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelChildServiceImpl implements ExcelChildService {

    private final ChildService childService;

    @Override
    @TrackHistory(action= Action.CREATE_CHILD_WITHEXCEL, targetType = Child.class, idParamName = "objectList")
    public List<ChildDto> createList(List<ChildDto> objectList) {
        List<ChildDto> childDtoList = new ArrayList<>();
        for (ChildDto object : objectList) {
            childDtoList.add(childService.create(object));
        }
        return childDtoList;
    }

    @Override
    public List<ChildDto> findAll() {
        return null;
    }

    @Override
    public ChildDto findById(Long aLong) {
        return null;
    }

    @Override
    public ChildDto create(ChildDto object) {
        return null;
    }

    @Override
    public ChildDto update(ChildDto object) {
        return null;
    }

    @Override
    public void delete(ChildDto object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
