package com.example.sunny.repository;

import com.example.sunny.model.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentsRepository extends JpaRepository<Parents, Long> {
    Parents findByName(String name);
}
