package com.example.sunny.service;

import com.example.sunny.model.dto.ChildDto;

import java.util.List;

public interface ChildService extends CrudService<ChildDto, Long> {
    ChildDto findByName(String name);

    List<ChildDto> findChildWithBirthMonth(int month);

    List<ChildDto> checkChild(ChildDto childDto);

    List<ChildDto> getAttendingChildren();
    List<ChildDto> updateChildrenClass(List<ChildDto> childrenList, String className);
}
