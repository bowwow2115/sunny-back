package com.example.sunny.service;

import com.example.sunny.model.User;
import com.example.sunny.model.dto.UserDto;

public interface UserService extends CrudService<UserDto, Long> {
    User findUserByUserId(String userId);
}
