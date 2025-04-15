package com.example.sunny.repository;

import com.example.sunny.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long>, ChildRepositoryCustom {
    List<Child> findByName(String name);
}
