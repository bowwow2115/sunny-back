package com.sunny.repository;

import com.sunny.model.Parents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParentsRepository extends JpaRepository<Parents, Long> {
    List<Parents> findByName(String name);
}
