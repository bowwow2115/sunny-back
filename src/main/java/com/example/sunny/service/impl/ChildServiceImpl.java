package com.example.sunny.service.impl;

import com.example.sunny.model.Child;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;

    @Override
    public Child findByName(String name) {
        return childRepository.findByname(name);
    }

    @Override
    public Set<Child> findAll() {
        Set<Child> children = new HashSet<>();
        childRepository.findAll().forEach(children::add);
        return children;
    }

    @Override
    public Child findById(Long aLong) {
        return childRepository.findById(aLong).orElse(null);
    }

    @Override
    public Child save(Child object) {
        return childRepository.save(object);
    }

    @Override
    public void delete(Child object) {
        childRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        childRepository.deleteById(aLong);
    }
}
