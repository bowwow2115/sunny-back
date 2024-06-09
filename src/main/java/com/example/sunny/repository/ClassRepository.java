package com.example.sunny.repository;

import com.example.sunny.model.SunnyClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<SunnyClass, Long> {
}
