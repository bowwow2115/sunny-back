package com.sunny.config.auth.oauth2;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response")); // 네이버는 response 키 안에 있음
    }
    @Override public String getProviderId() { return (String) attributes.get("id"); }
    @Override public String getEmail() { return (String) attributes.get("email"); }
    @Override public String getName() { return (String) attributes.get("name"); }
    @Override public String getPicture() { return (String) attributes.get("profile_image"); }
}