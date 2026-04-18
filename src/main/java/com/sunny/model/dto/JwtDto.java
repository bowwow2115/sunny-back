package com.sunny.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
public class JwtDto {
    @Size(max = 500, message = "액세스 토큰은 500자 이하로 입력해야 합니다.")
    private String accessToken;

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    @Size(max = 500, message = "리프레시 토큰은 500자 이하로 입력해야 합니다.")
    private String refreshToken;

    @Size(max = 50, message = "사용자 ID는 50자 이하로 입력해야 합니다.")
    private String userId;

    @Size(max = 100, message = "역할은 100자 이하로 입력해야 합니다.")
    private String roles;
}
