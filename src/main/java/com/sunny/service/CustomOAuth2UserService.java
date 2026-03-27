package com.sunny.service;

import com.sunny.config.auth.oauth2.CustomOAuth2User;
import com.sunny.config.auth.oauth2.OAuth2UserInfo;
import com.sunny.config.auth.oauth2.OAuth2UserInfoFactory;
import com.sunny.model.User;
import com.sunny.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP 요청용

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. OAuth2 제공자로부터 사용자 정보 조회
        OAuth2User oAuth2User = loadUserProfile(userRequest);

        // 2. 제공자 구분 (google, naver, kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. 사용자 식별 키 속성명 (예: id, sub 등)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 4. 응답 속성을 담을 DTO 생성
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        // 5. DB 에 사용자 저장 또는 업데이트
        User user = saveOrUpdate(userInfo, registrationId);

        // 6. Spring Security 세션에 저장할 사용자 객체 반환
        return new CustomOAuth2User(user, oAuth2User);
    }

    // 사용자 정보를 직접 조회하는 메서드
    private OAuth2User loadUserProfile(OAuth2UserRequest userRequest) {
        // 사용자 정보 엔드포인트 URI 가져오기
        String userInfoUri = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();

        // 액세스 토큰 가져오기
        String accessToken = userRequest.getAccessToken().getTokenValue();

        // HTTP 헤더 설정 (Bearer Token)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // GET 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                Map.class
        );

        // 반환된 속성으로 OAuth2User 객체 생성 (기본 구현 사용)
        return new DefaultOAuth2User(
                Collections.emptyList(),
                response.getBody(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private User saveOrUpdate(OAuth2UserInfo userInfo, String registrationId) {
        // 기존 사용자 찾기
        Optional<User> findUser = userRepository.findByProviderAndProviderId(registrationId, userInfo.getProviderId());

        if(findUser.isEmpty()) {
            // 이메일이 이미 존재하는지 확인
            Optional<User> emailUser = userRepository.findByEmail(userInfo.getEmail());
            if(emailUser.isPresent()) {
                User user = emailUser.get();
                if(user.getProvider().equals(User.Provider.LOCAL)) { // 로컬 계정으로 등록된 사용자인 경우 연결
                    user.setProvider(User.Provider.valueOf(registrationId.toUpperCase()));
                    user.setProviderId(userInfo.getProviderId());
                    user = user.update(userInfo.getName(), userInfo.getPicture());
                    userRepository.save(user);
                } else    // 다른 OAuth2 제공자로 등록된 사용자인 경우 예외 처리
                    throw new IllegalArgumentException("이미 " + emailUser.get().getProvider() + " 계정으로 등록된 이메일입니다.");
            }
        }

        User update = findUser.get().update(userInfo.getName(), userInfo.getPicture());
        return userRepository.save(update);
    }
}