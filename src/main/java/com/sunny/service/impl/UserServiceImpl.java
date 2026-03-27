package com.sunny.service.impl;

import com.sunny.code.Action;
import com.sunny.config.aop.TrackHistory;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.User;
import com.sunny.model.dto.UserDto;
import com.sunny.repository.UserRepository;
import com.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @TrackHistory(action = Action.FIND_USER_ALL, targetType = User.class, noTargetId = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @TrackHistory(action = Action.FIND_USER_BYID, targetType = User.class, idParamName = "aLong")
    public UserDto findById(Long aLong) {
        User user = userRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new UserDto(user);
    }

    @Override
    @Transactional
    @TrackHistory(action = Action.CREATE_USER, targetType = User.class, idParamName = "object")
    public UserDto create(UserDto object) {
        User user = null;
        try {
            user = userRepository.save(
                    User.builder()
                            .telephone(object.getTelephone())
                            .email(object.getEmail())
                            .userId(object.getUserId())
                            .name(object.getUserName())
                            .status(false) //회원가입 시 기본적으로 비활성화 상태로 생성, 관리자가 활성화 시켜야 로그인 가능
                            .password(passwordEncoder.encode(object.getPassword()))
                            .role(User.Role.USER) //회원가입유저는 일반유저로 고정
                            .build()
            );
        } catch (ConstraintViolationException e) {
            throw new BusinessException(ErrorCode.USERALREADYEXISTS);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.USERALREADYEXISTS);
        }
        return new UserDto(user);
    }

    @Override
    @Transactional
    @TrackHistory(action = Action.UPDATE_USER, targetType = User.class, idParamName = "object")
    public UserDto update(UserDto object) {
        User origin = userRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업데이트할 유저가 존재하지 않습니다."));

        if(!object.getPassword().isBlank()) origin.setPassword(passwordEncoder.encode(object.getPassword()));

        origin.setStatus(object.isStatus());
        origin.setEmail(object.getEmail());
        origin.setTelephone(object.getTelephone());

        if(origin.getRole().equals(User.Role.USER)) origin.setRole(User.Role.USER); // 유저는 role 속성 변경 불가능

        return new UserDto(userRepository.save(origin));
    }

    @Override
    @Transactional
    @TrackHistory(action = Action.DELETE_USER, targetType = User.class, idParamName = "object")
    public void delete(UserDto object) {
        userRepository.delete(object.toEntity());
    }

    @Override
    @Transactional
    @TrackHistory(action = Action.DELETE_USER, targetType = User.class, idParamName = "aLong")
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    @Override
    @TrackHistory(action = Action.FIND_USER_BYID, targetType = User.class)
    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }
}
