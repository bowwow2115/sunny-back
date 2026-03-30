package com.sunny.config.auth.oauth2;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private String userNameAttributeName;
    public GoogleOAuth2UserInfo(Map<String, Object> attributes, String userNameAttributeName) {
        super(attributes);
        this.userNameAttributeName = userNameAttributeName;
        if(!getEmailVerified()) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_APPROVED, "Google 계정의 이메일이 인증되지 않았습니다.");
        }
    }
    @Override public String getProviderId() { return (String) attributes.get(this.userNameAttributeName); }
    @Override public String getEmail() { return (String) attributes.get("email"); }
    @Override public String getName() { return (String) attributes.get("name"); }
    @Override public String getPicture() { return (String) attributes.get("profile_image"); }
    public Boolean getEmailVerified() { return (Boolean) attributes.get("email_verified"); }
}
