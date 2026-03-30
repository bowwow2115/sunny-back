package com.sunny.repository;

import com.sunny.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    User findUserByUserId(String userId);
    Optional<User> findByProviderAndProviderId(User.Provider provider, String providerId);
    Optional<User> findByEmail(String email);
}
