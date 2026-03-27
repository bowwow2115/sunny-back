package com.sunny.repository;

import com.sunny.model.User;

import java.util.Optional;

public interface UserCustomRepository {
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findByEmail(String email);
}
