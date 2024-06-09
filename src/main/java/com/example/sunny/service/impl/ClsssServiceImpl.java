package com.example.sunny.service.impl;

import com.example.sunny.model.SunnyClass;
import com.example.sunny.repository.ClassRepository;
import com.example.sunny.service.ClassServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClsssServiceImpl implements ClassServcie {

    private final ClassRepository classRepository;

    @Override
    public List<SunnyClass> findAll() {
//        Set<SunnyClass> sunnyClasses = new HashSet<>();
//        classRepository.findAll().forEach(sunnyClasses::add);
        return classRepository.findAll();
    }

    @Override
    public SunnyClass findById(Long aLong) {
        return classRepository.findById(aLong).orElse(null);
    }

    @Override
    public SunnyClass save(SunnyClass object) {
        return classRepository.save(object);
    }

    @Override
    public void delete(SunnyClass object) {
        classRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        classRepository.deleteById(aLong);
    }
}
