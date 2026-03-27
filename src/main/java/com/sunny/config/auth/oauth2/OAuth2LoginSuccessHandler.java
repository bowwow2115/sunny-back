package com.sunny.config.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.config.auth.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰 발급을 위한 유틸 클래스
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Map<String,Object> result =new HashMap();

        // JWT 발급 로직 (예시)
        String accessToken = jwtTokenUtil.generateToken(oAuth2User);
        String refreshToken = jwtTokenUtil.generateRefreshToken(oAuth2User);

        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("userId", oAuth2User.getUsername());
        result.put("roles", oAuth2User.getAuthorities());

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
    }

}