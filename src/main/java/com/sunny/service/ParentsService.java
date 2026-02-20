package com.sunny.service;

import com.sunny.model.dto.ParentsDto;

import java.util.List;

public interface ParentsService extends CrudService<ParentsDto, Long> {
    List<ParentsDto> findByName(String name);
}
