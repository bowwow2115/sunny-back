package com.example.sunny.repository;

import com.example.sunny.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long>, ChildRepositoryCustom {
    Child findByName(String name);
}
