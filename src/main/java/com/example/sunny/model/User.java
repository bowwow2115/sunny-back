package com.example.sunny.model;

import com.example.sunny.model.dto.UserDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sunny_user")
public class User extends Person {
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "status", nullable = false)
    private boolean status;
    @Builder
    public User(Long id, String name, String userId, String password, String role, boolean status) {
        super(id, name);
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.status = status;
    }
}
