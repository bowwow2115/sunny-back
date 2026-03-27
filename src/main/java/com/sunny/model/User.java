package com.sunny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sunny_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "providerId"})
})
public class User extends Person {
    @Column(name = "user_id", nullable = false, unique = true, length = 20)
    private String userId;
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name = "status", nullable = false)
    private boolean status;
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "telephone", nullable = false, length = 15)
    private String telephone;
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 20)
    @ColumnDefault("'LOCAL'")
    private Provider provider = Provider.LOCAL;

    // 제공자에서 부여하는 고유 ID
    @Column(name = "providerId")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    @ColumnDefault("'USER'")
    private Role role = Role.USER;
    private String picture; // 프로필 이미지

    public enum Role {
        GUEST, USER, ADMIN
    }

    public enum Provider {
        GOOGLE, NAVER, KAKAO, LOCAL
    }
    @Builder
    public User(Long id, String createdBy, String modifiedBy, String name, String userId, String password, boolean status, String email, String telephone, Provider provider, String providerId, Role role, String picture) {
        super(id, createdBy, modifiedBy, name);
        this.userId = userId;
        this.password = password;
        this.status = status;
        this.email = email;
        this.telephone = telephone;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.picture = picture;
    }



    public User update(String name, String picture) {
        super.setName(name);
        this.picture = picture;
        return this;
    }

}
