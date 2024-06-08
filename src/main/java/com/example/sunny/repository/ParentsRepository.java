package com.example.sunny.repository;

import com.example.sunny.model.Parents;
import org.springframework.data.repository.CrudRepository;

public interface ParentsRepository extends CrudRepository<Parents, Long> {
    Parents findByName(String name);
}
