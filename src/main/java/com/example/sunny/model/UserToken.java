package com.example.sunny.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
//@Entity
//@Table(name = "sunny_user_token")
public class UserToken{
    private Date transDate;
    private String userId;
    private String accessToken;
    private String refreshToken;
    private Date ExpiredDate;
    private String userIp;
}
