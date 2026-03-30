package com.sunny.model.dto;

import com.sunny.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "사용자 아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "사용자 아이디는 4자 이상 20자 이하로 입력해야 합니다.")
    private String userId;
    @NotBlank
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    @NotBlank
    @Size(min = 2, max = 50, message = "사용자 이름은 2자 이상 50자 이하로 입력해야 합니다.")
    private String userName;
    private User.Role role;
    @Size(max = 15, message = "전화번호는 최대 15자까지 입력할 수 있습니다.")
    private String telephone;
    @Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
    private String email;

    private User.Provider provider = User.Provider.LOCAL;
    private String providerId;
    private String picture;

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
        this.provider = user.getProvider();
        this.providerId = user.getProviderId();
        this.picture = user.getPicture();
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
                .provider(provider)
                .providerId(providerId)
                .picture(picture)
                .build();
    }

}
