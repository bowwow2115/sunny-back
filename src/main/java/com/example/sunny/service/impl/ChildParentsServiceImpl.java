package com.example.sunny.service.impl;

import com.example.sunny.model.ChildParents;
import com.example.sunny.model.dto.ChildParentsDto;
import com.example.sunny.repository.ChildParentsRepository;
import com.example.sunny.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildParentsServiceImpl implements CrudService<ChildParentsDto, Long> {

    private final ChildParentsRepository childParentsRepository;
    @Override
    public List<ChildParentsDto> findAll() {
        return null;
    }

    @Override
    public ChildParentsDto findById(Long aLong) {
        return null;
    }

    @Override
    public ChildParentsDto create(ChildParentsDto object) {
        return childParentsRepository.save(object);
    }

    @Override
    public ChildParentsDto update(ChildParentsDto object) {
        return childParentsRepository.save(object);
    }

    @Override
    public void delete(ChildParentsDto object) {
        childParentsRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        childParentsRepository.deleteById(aLong);
    }
}
