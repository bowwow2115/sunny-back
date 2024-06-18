package com.example.sunny.model.dto;

import com.example.sunny.model.User;
import lombok.*;

import javax.persistence.Column;
import java.lang.reflect.Member;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userId;
    private String password;
    private String userName;
    private String role;
    private String telephone;
    private String email;
    private boolean status;

    public UserDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.userName = user.getName();
        this.role = user.getRole();
        this.status = user.isStatus();
        this.telephone = user.getTelephone();
        this.email = user.getEmail();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .userId(userId)
                .password(password)
                .name(userName)
                .status(status)
                .role(role)
                .email(email)
                .telephone(telephone)
                .build();
    }

}
