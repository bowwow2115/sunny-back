package com.example.sunny.config.jwt;

import com.example.sunny.util.ServletUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60 ;
    private static final long REFRESH_TOKEN_VALIDITY =1 * 24 * 60 * 60;

    @Value("${jwt.jwtAccessHours}")
    private long jwtAccessHours;
    @Value("${jwt.jwtRefreshDay}")
    private long jwtRefreshDay;

    @Value("${jwt.secret}")
    private String secret;

    //TODO:정식 배포 시 위에 jwt secret 삭제 후 해제
//    @PostConstruct
//    private void secretKeyGen() {
//        KeyGenerator keyGen = null;
//        try {
//            keyGen = KeyGenerator.getInstance("HmacSHA512");
//        } catch (NoSuchAlgorithmException e) {
//            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//        SecretKey secretKey = keyGen.generateKey();
//        // Secret Key를 Base64로 인코딩하여 문자열로 저장
//        this.secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
//        UserVo userVo = (UserVo) userDetails;
//        claims.put("role", userVo.getAuthCode());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
//        UserVo userVo = (UserVo) userDetails;
//        claims.put("role", userVo.getAuthCode());
        return doGenerateRefreshToken(claims,  userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (jwtAccessHours*JWT_TOKEN_VALIDITY * 1000)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (jwtRefreshDay*REFRESH_TOKEN_VALIDITY * 1000)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
//        UserVo userVo = (UserVo) userDetails;
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("role");
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String jwtToken = null;
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer "))
            jwtToken = tokenHeader.substring(7);
        else if (ServletUtil.isInCookies(request.getCookies(), "auth"))
            jwtToken = ServletUtil.isInCookiesValue(request.getCookies(), "auth");
        return jwtToken;
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean getIsAdmin(String token) {
        String role = this.extractRole(token);
        return "admin".equals(role);
    }

    public Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

}
