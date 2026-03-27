package com.sunny.config.auth.oauth2;

import com.sunny.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private final User user;
    private final OAuth2User oAuth2User;
    private final Map<String, Object> attributes;

    /**
     * 생성자
     * @param user DB 에 저장된 사용자 엔티티
     * @param attributes OAuth2 제공자로부터 받은 사용자 속성
     */
    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.oAuth2User = null;
        this.attributes = attributes;
    }

    /**
     * 생성자 (OAuth2User 위임용)
     * @param user DB 에 저장된 사용자 엔티티
     * @param oAuth2User Spring Security 의 OAuth2User 객체
     */
    public CustomOAuth2User(User user, OAuth2User oAuth2User) {
        this.user = user;
        this.oAuth2User = oAuth2User;
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (oAuth2User != null) {
            return oAuth2User.getAuthorities();
        }
        // 사용자 역할 (Role) 을 권한으로 변환
        return java.util.Collections.singletonList(
            (GrantedAuthority) () -> "ROLE_" + user.getRole()
        );
    }

    @Override
    public String getName() {
        if (oAuth2User != null) {
            return oAuth2User.getName();
        }
        return user.getProviderId();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 추가 메서드: 사용자 이메일 조회
     */
    public String getEmail() {
        return user.getEmail();
    }



    /**
     * 추가 메서드: 제공자 조회 (google, naver, kakao)
     */
    public String getProvider() {
        return user.getProvider().name();
    }

    /**
     * 추가 메서드: 제공자 ID 조회
     */
    public String getProviderId() {
        return user.getProviderId();
    }

    /**
     * 추가 메서드: 사용자 역할 조회
     */
    public String getRole() {
        return user.getRole().name();
    }
}