package com.example.sunny.service.impl;

import com.example.sunny.model.ChildParents;
import com.example.sunny.repository.ChildParentsRepository;
import com.example.sunny.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildParentsServiceImpl implements CrudService<ChildParents, Long> {

    private final ChildParentsRepository childParentsRepository;
    @Override
    public List<ChildParents> findAll() {
        return null;
    }

    @Override
    public ChildParents findById(Long aLong) {
        return null;
    }

    @Override
    public ChildParents save(ChildParents object) {
        return childParentsRepository.save(object);
    }

    @Override
    public void delete(ChildParents object) {
        childParentsRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        childParentsRepository.deleteById(aLong);
    }
}
