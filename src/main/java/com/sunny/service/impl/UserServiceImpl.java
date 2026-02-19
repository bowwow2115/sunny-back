package com.sunny.service.impl;

import com.sunny.code.SunnyCode;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long aLong) {
        User user = userRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new UserDto(user);
    }

    @Override
    public UserDto create(UserDto object) {
        User user = null;
        try {
            user = userRepository.save(
                    User.builder()
                            .telephone(object.getTelephone())
                            .email(object.getEmail())
                            .userId(object.getUserId())
                            .name(object.getUserName())
                            .status(false)
                            .password(passwordEncoder.encode(object.getPassword()))
//                            TODO: 배포 시 변경해야함
//                            .role(object.getRole())
                            .role(SunnyCode.ROLE_GENERAL_USER)
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
    public UserDto update(UserDto object) {
        User origin = userRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업데이트할 유저가 존재하지 않습니다."));

        if(!object.getPassword().isBlank()) origin.setPassword(passwordEncoder.encode(object.getPassword()));

        origin.setStatus(object.isStatus());
        origin.setEmail(object.getEmail());
        origin.setTelephone(object.getTelephone());

        if(origin.getRole().equals(SunnyCode.ROLE_GENERAL_ADMIN)) origin.setRole(object.getRole());

        return new UserDto(userRepository.save(origin));
    }

    @Override
    public void delete(UserDto object) {
        userRepository.delete(object.toEntity());
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    @Override
    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }
}
