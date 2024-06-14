package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.User;
import com.example.sunny.model.dto.UserDto;
import com.example.sunny.repository.UserRepository;
import com.example.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long aLong) {
        User user = userRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new UserDto(user);
    }

    @Override
    public UserDto create(UserDto object) {
        return new UserDto(userRepository.save(object.toEntity()));
    }

    @Override
    public UserDto update(UserDto object) {
        return new UserDto(userRepository.save(object.toEntity()));
    }

    @Override
    public void delete(UserDto object) {
        userRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }
}
