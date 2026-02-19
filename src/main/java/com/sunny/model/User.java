package com.sunny.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Getter
@Setter
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
    public User(Long id, String createdBy, String modifiedBy, String name, String userId, String password, String role, boolean status, String email, String telephone) {
        super(id, createdBy, modifiedBy, name);
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.status = status;
        this.email = email;
        this.telephone = telephone;
    }
}
