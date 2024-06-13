package com.example.sunny.service;

import com.example.sunny.model.Parents;
import com.example.sunny.model.dto.ParentsDto;

public interface ParentsService extends CrudService<ParentsDto, Long> {
    ParentsDto findByName(String name);
}
