package com.sunny.service;

import com.sunny.model.dto.ParentsDto;

public interface ParentsService extends CrudService<ParentsDto, Long> {
    ParentsDto findByName(String name);
}
