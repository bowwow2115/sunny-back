package com.sunny.repository;

import com.sunny.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    User findUserByUserId(String userId);
}
