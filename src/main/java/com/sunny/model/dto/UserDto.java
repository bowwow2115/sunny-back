package com.sunny.model.dto;

import com.sunny.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userId;
    private String userName;
    private User.Role role;
    private String telephone;
    private String email;

    @Builder.Default
    private User.Provider provider = User.Provider.LOCAL;
    private String providerId;
    private String picture;

    private boolean status;

    public UserDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userName = user.getName();
        this.role = user.getRole();
        this.status = user.isStatus();
        this.telephone = user.getTelephone();
        this.email = user.getEmail();
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.picture = user.getPicture();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .userId(userId)
                .name(userName)
                .status(status)
                .role(role)
                .email(email)
                .telephone(telephone)
                .provider(provider)
                .providerId(providerId)
                .picture(picture)
                .build();
    }

}
