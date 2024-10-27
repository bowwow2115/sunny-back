package com.example.sunny.service;

import com.example.sunny.model.dto.ChildDto;

import java.util.List;
import java.util.Map;

public interface ChildService extends CrudService<ChildDto, Long> {
    ChildDto findByName(String name);

    Map getUnRidedChildren();

    List<ChildDto> findChildWithBirthMonth(int month);

    List<ChildDto> checkChild(ChildDto childDto);

    List<ChildDto> getAttendingChildren();
    List<ChildDto> updateChildrenClass(List<ChildDto> childrenList, String className);
}
