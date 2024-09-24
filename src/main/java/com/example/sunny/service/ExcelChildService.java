package com.example.sunny.service;

import com.example.sunny.model.dto.ChildDto;

import java.util.List;

public interface ExcelChildService extends CrudService<ChildDto, Long> {
    List<ChildDto> createList(List<ChildDto> childDtoList);

}
