package com.sunny.controller;

import com.sunny.config.auth.oauth2.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("로그인 필요");
        }

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of(
            "email", user.getEmail(),
            "provider", user.getProvider(),
            "providerId", user.getUser().getProviderId(),
            "role", user.getUser().getRole()
        ));
    }
}