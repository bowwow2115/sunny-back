package com.example.sunny.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityUtils {
    public static boolean isAdmin() {
        // 현재 인증된 사용자의 Authentication 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false; // 인증된 사용자가 없는 경우
        }

        // 사용자의 권한 목록을 스트림으로 처리하여 "ROLE_ADMIN" 권한이 있는지 확인
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
