package com.sunny.service;

import com.sunny.code.SunnyCode;
import com.sunny.config.error.BusinessException;
import com.sunny.model.User;
import com.sunny.model.dto.UserDto;
import com.sunny.repository.UserRepository;
import com.sunny.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위테스트")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // ==================== findAll ====================
    @Test
    @DisplayName("모든 사용자 조회 - 성공")
    void testFindAll_Success() {
        // Given
        User user1 = User.builder()
                .id(1L)
                .userId("user1")
                .name("김철수")
                .email("kim@example.com")
                .telephone("010-1234-5678")
                .password("encodedPassword1")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .userId("user2")
                .name("이영희")
                .email("lee@example.com")
                .telephone("010-9876-5432")
                .password("encodedPassword2")
                .status(false)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        List<User> userList = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // When
        List<UserDto> result = userService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo("user1");
        assertThat(result.get(1).getUserId()).isEqualTo("user2");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모든 사용자 조회 - 빈 리스트")
    void testFindAll_Empty() {
        // Given
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<UserDto> result = userService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    // ==================== findById ====================
    @Test
    @DisplayName("ID로 사용자 조회 - 성공")
    void testFindById_Success() {
        // Given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .userId("user1")
                .name("김철수")
                .email("kim@example.com")
                .password("password")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        UserDto result = userService.findById(userId);

        // Then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUserId()).isEqualTo("user1");
        assertThat(result.getUserName()).isEqualTo("김철수");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("ID로 사용자 조회 - 사용자 없음")
    void testFindById_UserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(BusinessException.class);
        verify(userRepository, times(1)).findById(userId);
    }

    // ==================== create ====================
    @Test
    @DisplayName("사용자 생성 - 성공")
    void testCreate_Success() {
        // Given
        UserDto userDto = UserDto.builder()
                .userId("newuser")
                .userName("박새로운")
                .email("newuser@example.com")
                .telephone("010-1111-1111")
                .password("rawPassword")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .userId("newuser")
                .name("박새로운")
                .email("newuser@example.com")
                .telephone("010-1111-1111")
                .password("encodedPassword")
                .status(false)
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.create(userDto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo("newuser");
        assertThat(result.getUserName()).isEqualTo("박새로운");
        assertThat(result.isStatus()).isFalse();
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 - 중복된 사용자")
    void testCreate_UserAlreadyExists() {
        // Given
        UserDto userDto = UserDto.builder()
                .userId("existinguser")
                .userName("기존사용자")
                .email("existing@example.com")
                .password("password")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenThrow(new org.hibernate.exception.ConstraintViolationException("Unique constraint", null, ""));

        // When & Then
        assertThatThrownBy(() -> userService.create(userDto))
                .isInstanceOf(BusinessException.class);
    }

    // ==================== update ====================
    @Test
    @DisplayName("사용자 업데이트 - 성공")
    void testUpdate_Success() {
        // Given
        Long userId = 1L;
        UserDto updateDto = UserDto.builder()
                .id(userId)
                .userId("user1")
                .userName("김철수")
                .email("newemail@example.com")
                .telephone("010-9999-9999")
                .password("newPassword")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_ADMIN)
                .build();

        User existingUser = User.builder()
                .id(userId)
                .userId("user1")
                .name("김철수")
                .email("oldemail@example.com")
                .telephone("010-1234-5678")
                .password("oldPassword")
                .status(false)
                .role(SunnyCode.ROLE_GENERAL_ADMIN)
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .userId("user1")
                .name("김철수")
                .email("newemail@example.com")
                .telephone("010-9999-9999")
                .password("encodedNewPassword")
                .status(true)
                .role(SunnyCode.ROLE_GENERAL_ADMIN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.update(updateDto);

        // Then
        assertThat(result.getEmail()).isEqualTo("newemail@example.com");
        assertThat(result.getTelephone()).isEqualTo("010-9999-9999");
        assertThat(result.isStatus()).isTrue();
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 업데이트 - 빈 비밀번호")
    void testUpdate_EmptyPassword() {
        // Given
        Long userId = 1L;
        UserDto updateDto = UserDto.builder()
                .id(userId)
                .userId("user1")
                .userName("김철수")
                .email("newemail@example.com")
                .password("")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .status(true)
                .build();

        User existingUser = User.builder()
                .id(userId)
                .userId("user1")
                .name("김철수")
                .password("oldPassword")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .status(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        UserDto result = userService.update(updateDto);

        // Then
        assertThat(result.getEmail()).isEqualTo("newemail@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 업데이트 - 사용자 없음")
    void testUpdate_UserNotFound() {
        // Given
        UserDto updateDto = UserDto.builder()
                .id(999L)
                .password("newPassword")
                .build();

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.update(updateDto))
                .isInstanceOf(BusinessException.class);
    }

    // ==================== delete ====================
    @Test
    @DisplayName("사용자 삭제 - 성공 (delete)")
    void testDelete_Success() {
        // Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .userId("user1")
                .userName("김철수")
                .build();

        // When
        userService.delete(userDto);

        // Then
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 - 성공 (deleteById)")
    void testDeleteById_Success() {
        // Given
        Long userId = 1L;

        // When
        userService.deleteById(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    // ==================== findUserByUserId ====================
    @Test
    @DisplayName("사용자명으로 사용자 조회 - 성공")
    void testFindUserByUserId_Success() {
        // Given
        String userId = "testuser";
        User user = User.builder()
                .id(1L)
                .userId(userId)
                .name("테스트사용자")
                .email("test@example.com")
                .password("password")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        when(userRepository.findUserByUserId(userId)).thenReturn(user);

        // When
        User result = userService.findUserByUserId(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("테스트사용자");
        verify(userRepository, times(1)).findUserByUserId(userId);
    }

    @Test
    @DisplayName("사용자명으로 사용자 조회 - 사용자 없음")
    void testFindUserByUserId_UserNotFound() {
        // Given
        String userId = "notfound";
        when(userRepository.findUserByUserId(userId)).thenReturn(null);

        // When
        User result = userService.findUserByUserId(userId);

        // Then
        assertThat(result).isNull();
        verify(userRepository, times(1)).findUserByUserId(userId);
    }
}
