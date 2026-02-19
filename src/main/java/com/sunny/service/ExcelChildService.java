package com.sunny.service;

import com.sunny.model.dto.ChildDto;

import java.util.List;

public interface ExcelChildService extends CrudService<ChildDto, Long> {
    List<ChildDto> createList(List<ChildDto> childDtoList);

}
