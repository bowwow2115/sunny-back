package com.example.sunny.config.jwt;

import com.example.sunny.service.AuthUserDetailsService;
import com.example.sunny.util.ServletUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AuthUserDetailsService authUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


        String id = null;
        String jwtToken = null;
        String tokenHeader = request.getHeader("Authorization");

        // check 'Authorization' header
        // check JWT token
        if (tokenHeader != null && tokenHeader.startsWith("Bearer "))
            jwtToken = tokenHeader.substring(7);
        else if (ServletUtil.isInCookies(request.getCookies(), "auth"))
            jwtToken = ServletUtil.isInCookiesValue(request.getCookies(), "auth");

        if (jwtToken == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            id = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return;
        } catch (ExpiredJwtException ee) { // 토큰 만료?
            log.error(ee.getMessage());
            // 프론트 화면 단에서 쿠키 가지고 있을 수 있어서 키우완 브런치 이동 후 삭제 예정.
            Cookie cookie = ServletUtil.getCookies(request.getCookies(), "auth");
            if (cookie != null) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            if (request.getServletPath().contentEquals("/")) {
                log.info("ExpiredJwtException : {} ", "getServletPath /");
                request.getRequestDispatcher("/").forward(request, response);
            } else {
                log.info("ExpiredJwtException : {} ", request.getServletPath());
                request.getRequestDispatcher("/exception/jwt").forward(request, response);
            }
            return;
        }

        // check get user ID by jwt Token
        if (id == null) {
            chain.doFilter(request, response);
            return;
        }

        // check Auth and Set Auth
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.authUserDetailsService.loadUserByUsername(id);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}

