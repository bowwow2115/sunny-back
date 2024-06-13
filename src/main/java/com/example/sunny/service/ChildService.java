package com.example.sunny.service;

import com.example.sunny.model.Child;
import com.example.sunny.model.dto.ChildDto;

public interface ChildService extends CrudService<ChildDto, Long> {
    ChildDto findByName(String name);
}
