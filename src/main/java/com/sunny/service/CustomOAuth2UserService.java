package com.sunny.service;

import com.sunny.config.auth.oauth2.CustomOAuth2User;
import com.sunny.config.auth.oauth2.OAuth2UserInfo;
import com.sunny.config.auth.oauth2.OAuth2UserInfoFactory;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
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
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadUserProfile(userRequest);

        User.Provider registrationId = User.Provider.valueOf(
                userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        User user = saveOrUpdate(userInfo, registrationId);
        return new CustomOAuth2User(user, oAuth2User);
    }

    private OAuth2User loadUserProfile(OAuth2UserRequest userRequest) {
        String userInfoUri = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();

        String accessToken = userRequest.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return new DefaultOAuth2User(
                Collections.emptyList(),
                response.getBody(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private User saveOrUpdate(OAuth2UserInfo userInfo, User.Provider registrationId) {
        Optional<User> findUser = userRepository.findByProviderAndProviderId(registrationId, userInfo.getProviderId());

        if (findUser.isEmpty()) {
            Optional<User> emailUser = userRepository.findByEmail(userInfo.getEmail());
            if (emailUser.isPresent()) {
                User user = emailUser.get();
                if (user.getProvider() == User.Provider.LOCAL) {
                    user.setProvider(registrationId);
                    user.setProviderId(userInfo.getProviderId());
                    user = user.update(userInfo.getName(), userInfo.getPicture());
                    return userRepository.save(user);
                }
                throw new BusinessException(ErrorCode.EMAILALREADYEXISTS);
            }
            throw new BusinessException(ErrorCode.USERNOTFOUND);
        }

        User update = findUser.get().update(userInfo.getName(), userInfo.getPicture());
        return userRepository.save(update);
    }
}
