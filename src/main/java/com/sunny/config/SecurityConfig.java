package com.sunny.config;

import com.sunny.config.auth.jwt.JwtAuthenticationEntryPoint;
import com.sunny.config.auth.jwt.JwtRequestFilter;
import com.sunny.config.auth.oauth2.OAuth2LoginSuccessHandler;
import com.sunny.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("#{'${server.cors.allowed-origin-patterns:http://localhost:8080}'.split(',')}")
    private List<String> allowedOriginPatterns;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    // 2. SecurityFilterChain: HttpSecurity 설정을 담당 (가장 큰 변화)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF 비활성화 (Stateless 구조이므로)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 권한 설정 (antMatchers -> requestMatchers로 변경됨)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login/**",
                                "/signup",
                                "/logout/**",
                                "/app/**",
                                "/test/**",
                                "/oauth2/**",
                                "/auth/jwt/refresh",
                                "/auth/validation"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oAuth2 -> oAuth2
//                        .loginPage("/login")
                        .authorizationEndpoint(endPoint -> endPoint
                            .baseUri("/oauth2/authorization") // OAuth2 로그인 요청 URL
                        )
//                        .redirectionEndpoint(endpoint -> endpoint
//                                .baseUri("/login/oauth2/code")  // 콜백 엔드포인트
//                        )
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(
                allowedOriginPatterns.stream()
                        .map(String::trim)
                        .filter(origin -> !origin.isEmpty())
                        .toList()
        );
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 정적 리소스 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
