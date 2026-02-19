package com.sunny.repository;

import com.sunny.model.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentsRepository extends JpaRepository<Parents, Long> {
    Parents findByName(String name);
}
