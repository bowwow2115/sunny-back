package com.sunny.repository;

import com.sunny.model.User;
import com.sunny.util.HashGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 유저아이디로 찾기 테스트")
    public void findByUserIdTest() {
        String s = HashGenerator.generateRandomHash(8);
        User user = User.builder()
                        .name(s)
                        .userId(s)
                        .password(s)
                        .status(true)
                        .telephone(s)
                        .email(s)
                        .build();

        userRepository.save(user);

        User userByUserId = userRepository.findUserByUserId(user.getUserId());

        assertThat(userByUserId.getUserId()).isEqualTo(user.getUserId());
    }
}
