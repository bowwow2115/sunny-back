package com.sunny.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    @NotBlank(message = "사용자 아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "사용자 아이디는 4자 이상 20자 이하로 입력해야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "사용자 이름은 2자 이상 50자 이하로 입력해야 합니다.")
    private String userName;

    @Size(max = 15, message = "전화번호는 최대 15자까지 입력할 수 있습니다.")
    private String telephone;

    @Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
    private String email;
}
