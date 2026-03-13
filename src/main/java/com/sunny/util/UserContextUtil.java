package com.sunny.util;

import com.sunny.config.auth.CustomUserDetails;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserContextUtil {

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    /**
     * 현재 로그인한 사용자 ID 추출
     * - SecurityContext 에서 인증 정보를 가져옴
     * - 비동기 (@Async) 호출 시에는 반드시 메인 스레드에서 값을 추출하여 파라미터로 전달해야 함
     */
    public String getCurrentUserId() {
        try {
            SecurityContext context = securityContextHolderStrategy.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new BusinessException(ErrorCode.AUTHEXCEPTION, "잘못된 접근 입니다.");
            }

            Object principal = authentication.getPrincipal();

            // UserDetails 구현체인 경우
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }

            // String 으로 직접 저장된 경우
            if (principal instanceof String) {
                return (String) principal;
            }

            // 커스텀 사용자 객체인 경우 (예: CustomUserDetails)
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getUserId();
            }

            return "UNKNOWN";

        } catch (Exception e) {
            // 보안 컨텍스트 접근 실패 시 시스템 계정으로 처리
            return "SYSTEM";
        }
    }

    /**
     * 현재 사용자 이름 (실명/닉네임) 추출
     */
    public String getCurrentUserName() {
        try {
            SecurityContext context = securityContextHolderStrategy.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return "System";
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getName();
            }

            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            }

            return "Unknown";

        } catch (Exception e) {
            return "System";
        }
    }

    /**
     * 현재 사용자 권한 (Role) 추출
     */
    public List<String> getCurrentUserRoles() {
        try {
            SecurityContext context = securityContextHolderStrategy.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Collections.emptyList();
            }

            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 인증 여부 확인
     */
    public boolean isAuthenticated() {
        try {
            SecurityContext context = securityContextHolderStrategy.getContext();
            Authentication authentication = context.getAuthentication();
            return authentication != null && authentication.isAuthenticated();
        } catch (Exception e) {
            return false;
        }
    }
}