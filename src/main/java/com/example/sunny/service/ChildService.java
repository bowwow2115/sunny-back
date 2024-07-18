package com.example.sunny.service;

import com.example.sunny.model.dto.ChildDto;

import java.util.Map;

public interface ChildService extends CrudService<ChildDto, Long> {
    ChildDto findByName(String name);

    Map getUnRidedChildren();
}
