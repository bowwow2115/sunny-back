package com.example.sunny.repository;

import com.example.sunny.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);
}
