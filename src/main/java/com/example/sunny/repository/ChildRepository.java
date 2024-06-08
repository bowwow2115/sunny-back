package com.example.sunny.repository;

import com.example.sunny.model.Child;
import org.springframework.data.repository.CrudRepository;

public interface ChildRepository extends CrudRepository<Child, Long> {
    Child findByname(String name);
}
