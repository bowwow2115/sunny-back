package com.example.sunny.model.dto;

import com.example.sunny.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String password;
    private String userName;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getName();
    }
}
