package com.sunny.service;

import com.sunny.model.User;
import com.sunny.model.dto.UserDto;

public interface UserService extends CrudService<UserDto, Long> {
    User findUserByUserId(String userId);
}
