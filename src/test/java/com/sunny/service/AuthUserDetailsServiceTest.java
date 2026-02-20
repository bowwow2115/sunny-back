package com.sunny.service;

import com.sunny.code.SunnyCode;
import com.sunny.model.User;
import com.sunny.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthUserDetailsService 단위테스트")
public class AuthUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthUserDetailsService authUserDetailsService;

    // ==================== loadUserByUsername ====================
    @Test
    @DisplayName("사용자명으로 사용자 조회 - 성공")
    void testLoadUserByUsername_Success() {
        // Given
        String userId = "testuser";
        User user = User.builder()
                .id(1L)
                .userId(userId)
                .name("테스트사용자")
                .email("test@example.com")
                .password("encodedPassword")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);

        // When
        UserDetails result = authUserDetailsService.loadUserByUsername(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userId);
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_" + SunnyCode.ROLE_GENERAL_USER);
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - 관리자 권한")
    void testLoadUserByUsername_AdminUser() {
        // Given
        String userId = "admin";
        User user = User.builder()
                .id(1L)
                .userId(userId)
                .name("관리자")
                .email("admin@example.com")
                .password("adminPassword")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_ADMIN)
                .build();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);

        // When
        UserDetails result = authUserDetailsService.loadUserByUsername(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userId);
        assertThat(result.getPassword()).isEqualTo("adminPassword");
        assertThat(result.getAuthorities()).extracting("authority")
                .containsExactly("ROLE_" + SunnyCode.ROLE_GENERAL_ADMIN);
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - 사용자 없음")
    void testLoadUserByUsername_UserNotFound() {
        // Given
        String userId = "notfound";
        when(userRepository.findUserByUserId(userId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> authUserDetailsService.loadUserByUsername(userId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - UserDetails 속성 확인")
    void testLoadUserByUsername_UserDetailsProperties() {
        // Given
        String userId = "testuser";
        User user = User.builder()
                .id(1L)
                .userId(userId)
                .name("테스트사용자")
                .email("test@example.com")
                .password("encodedPassword")
                .status(false)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);

        // When
        UserDetails result = authUserDetailsService.loadUserByUsername(userId);

        // Then
        assertThat(result.isAccountNonExpired()).isTrue();
        assertThat(result.isAccountNonLocked()).isTrue();
        assertThat(result.isCredentialsNonExpired()).isTrue();
        assertThat(result.isEnabled()).isTrue();
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - 빈 문자열")
    void testLoadUserByUsername_EmptyUsername() {
        // Given
        String userId = "";
        when(userRepository.findUserByUserId(userId)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> authUserDetailsService.loadUserByUsername(userId))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - 특수문자가 포함된 사용자명")
    void testLoadUserByUsername_SpecialCharacters() {
        // Given
        String userId = "test@user.com";
        User user = User.builder()
                .id(1L)
                .userId(userId)
                .name("이메일사용자")
                .email("test@user.com")
                .password("encodedPassword")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);

        // When
        UserDetails result = authUserDetailsService.loadUserByUsername(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userId);
        verify(userRepository, times(1)).findUserByUserId(userId);
    }
}
