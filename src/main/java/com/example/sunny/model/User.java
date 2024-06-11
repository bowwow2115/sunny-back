package com.example.sunny.model;

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
    @Column(name = "status")
    private boolean status;
    @Builder
    public User(String name, String userId, String password, boolean status) {
        super(name);
        this.userId = userId;
        this.password = password;
        this.status = status;
    }
}
