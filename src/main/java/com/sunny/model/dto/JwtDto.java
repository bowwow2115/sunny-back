package com.sunny.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String roles;
}
