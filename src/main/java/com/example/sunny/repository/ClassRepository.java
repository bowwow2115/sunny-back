package com.example.sunny.repository;

import com.example.sunny.model.SunnyClass;
import org.springframework.data.repository.CrudRepository;

public interface ClassRepository extends CrudRepository<SunnyClass, Long> {
}
