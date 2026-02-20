package com.sunny.repository;

import com.sunny.code.SunnyCode;
import com.sunny.model.User;
import com.sunny.util.HashGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
    "spring.jpa.properties.hibernate.format_sql=true",
    "spring.jpa.properties.hibernate.use_sql_comments=true"
})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private SessionFactory sessionFactory;
    private Statistics statistics;

    @BeforeEach
    void setUp() {
        sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
    }

    private void resetQueryCount() {
        statistics.clear();
    }

    private long getQueryCount() {
        return statistics.getPrepareStatementCount();
    }

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
                        .role(SunnyCode.ROLE_GENERAL_USER)
                        .build();

        userRepository.save(user);
        entityManager.clear();

        User foundUser = userRepository.findUserByUserId(s);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(s);
    }

    @Test
    @DisplayName("유저 ID로 찾기")
    public void findByIdTest() {
        String userId = "testuser_" + System.currentTimeMillis();
        User user = User.builder()
                .name("테스트유저")
                .userId(userId)
                .password("password123")
                .status(true)
                .telephone("010-1234-5678")
                .email("test@example.com")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId();
        entityManager.clear();

        User foundUser = userRepository.findById(id).orElse(null);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("모든 유저 조회")
    public void findAllTest() {
        for (int i = 1; i <= 3; i++) {
            User user = User.builder()
                    .name("사용자" + i)
                    .userId("user" + i + "_" + System.currentTimeMillis())
                    .password("password" + i)
                    .status(i % 2 == 0)
                    .telephone("010-" + String.format("%04d", i*1000) + "-5678")
                    .email("user" + i + "@example.com")
                    .role(SunnyCode.ROLE_GENERAL_USER)
                    .build();
            userRepository.save(user);
        }
        entityManager.clear();

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("유저 저장")
    public void saveUserTest() {
        String userId = "newuser_" + System.currentTimeMillis();
        User user = User.builder()
                .name("새로운사용자")
                .userId(userId)
                .password("password123")
                .status(false)
                .telephone("010-9999-9999")
                .email("new@example.com")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("유저 정보 수정")
    public void updateUserTest() {
        String userId = "updateuser_" + System.currentTimeMillis();
        User user = User.builder()
                .name("수정할사용자")
                .userId(userId)
                .password("oldpassword")
                .status(false)
                .telephone("010-1111-1111")
                .email("old@example.com")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId();
        entityManager.clear();

        User foundUser = userRepository.findById(id).orElseThrow();
        foundUser.setEmail("new@example.com");
        foundUser.setTelephone("010-2222-2222");
        entityManager.flush();

        assertThat(foundUser.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("유저 삭제")
    public void deleteUserTest() {
        String userId = "deleteuser_" + System.currentTimeMillis();
        User user = User.builder()
                .name("삭제할사용자")
                .userId(userId)
                .password("password")
                .status(true)
                .telephone("010-3333-3333")
                .email("delete@example.com")
                .role(SunnyCode.ROLE_GENERAL_USER)
                .build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId();
        entityManager.clear();

        User foundUser = userRepository.findById(id).orElseThrow();
        userRepository.delete(foundUser);
        entityManager.flush();

        assertThat(userRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("여러 유저 대량 저장")
    public void saveMultipleUsersTest() {
        List<User> users = List.of(
                User.builder().userId("bulk1_" + System.currentTimeMillis()).name("대량사용자1")
                        .email("bulk1@example.com").password("password").status(true)
                        .telephone("010-1111-1111").role(SunnyCode.ROLE_GENERAL_USER).build(),
                User.builder().userId("bulk2_" + System.currentTimeMillis()).name("대량사용자2")
                        .email("bulk2@example.com").password("password").status(true)
                        .telephone("010-2222-2222").role(SunnyCode.ROLE_GENERAL_USER).build(),
                User.builder().userId("bulk3_" + System.currentTimeMillis()).name("대량사용자3")
                        .email("bulk3@example.com").password("password").status(false)
                        .telephone("010-3333-3333").role(SunnyCode.ROLE_GENERAL_USER).build()
        );

        userRepository.saveAll(users);

        assertThat(users).allMatch(u -> u.getId() != null);
    }
}
