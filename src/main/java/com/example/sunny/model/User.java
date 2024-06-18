package com.example.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("user")
    private String role;
    @Column(name = "status", nullable = false)
    private boolean status;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "telephone", nullable = false)
    private String telephone;
    @Builder
    public User(Long id, String name, String userId, String password, String role, boolean status, String email, String telephone) {
        super(id, name);
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.status = status;
        this.email = email;
        this.telephone = telephone;
    }
}
