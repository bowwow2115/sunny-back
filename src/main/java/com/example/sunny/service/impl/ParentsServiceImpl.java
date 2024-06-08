package com.example.sunny.service.impl;

import com.example.sunny.model.Parents;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParentsServiceImpl implements ParentsService {
    private final ParentsRepository parentsRepository;

    @Override
    public Set<Parents> findAll() {
        Set<Parents> parents = new HashSet<>();
        parentsRepository.findAll().forEach(parents::add);
        return parents;
    }

    @Override
    public Parents findById(Long aLong) {
        return parentsRepository.findById(aLong).orElse(null);
    }

    @Override
    public Parents save(Parents object) {
        return parentsRepository.save(object);
    }

    @Override
    public void delete(Parents object) {
        parentsRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        parentsRepository.deleteById(aLong);
    }

    @Override
    public Parents findByName(String name) {
        return parentsRepository.findByName(name);
    }
}
