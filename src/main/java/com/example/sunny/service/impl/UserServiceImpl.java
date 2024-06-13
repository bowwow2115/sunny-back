package com.example.sunny.service.impl;

import com.example.sunny.model.User;
import com.example.sunny.model.dto.UserDto;
import com.example.sunny.repository.UserRepository;
import com.example.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public UserDto findById(Long aLong) {
        return null;
    }

    @Override
    public UserDto create(UserDto object) {
        return null;
    }

    @Override
    public UserDto update(UserDto object) {
        return null;
    }

    @Override
    public UserDto save(UserDto object) {
        User user = User.builder()
                .userId(object.getUserId())
                .password(passwordEncoder.encode(object.getPassword()))
                .name(object.getUserName())
                .status(true)
                .build();
         return new UserDto(userRepository.save(user));
    }

    @Override
    public void delete(UserDto object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
