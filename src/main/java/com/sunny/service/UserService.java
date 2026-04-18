package com.sunny.service;

import com.sunny.model.User;
import com.sunny.model.dto.UserSignupRequest;
import com.sunny.model.dto.UserDto;
import com.sunny.model.dto.UserUpdateRequest;

public interface UserService extends CrudService<UserDto, Long> {
    UserDto create(UserSignupRequest request);
    UserDto update(UserUpdateRequest request);
    User findUserByUserId(String userId);
}
