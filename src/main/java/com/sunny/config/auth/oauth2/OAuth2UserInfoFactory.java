package com.sunny.config.auth.oauth2;

import com.sunny.model.User;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(User.Provider registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId == User.Provider.GOOGLE) {
            return new GoogleOAuth2UserInfo(attributes, userNameAttributeName);
//        else if (registrationId.equalsIgnoreCase("kakao")) {
//            return new KakaoOAuth2UserInfo(attributes);
//        }
//        else if(registrationId.equalsIgnoreCase("naver")) {
//            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}