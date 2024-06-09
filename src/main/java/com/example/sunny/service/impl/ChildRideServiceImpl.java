package com.example.sunny.service.impl;

import com.example.sunny.model.ChildRide;
import com.example.sunny.repository.ChildRideRepository;
import com.example.sunny.service.ChildRideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildRideServiceImpl implements ChildRideService {
    private final ChildRideRepository childRideRepository;

    @Override
    public List<ChildRide> findAll() {
//        Set<ChildRide> childRides = new HashSet<>();
//        childRideRepository.findAll().forEach(childRides::add);
        return childRideRepository.findAll();
    }

    @Override
    public ChildRide findById(Long aLong) {
        return childRideRepository.findById(aLong).orElse(null);
    }

    @Override
    public ChildRide save(ChildRide object) {
        return childRideRepository.save(object);
    }

    @Override
    public void delete(ChildRide object) {
        childRideRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        childRideRepository.deleteById(aLong);
    }
}
