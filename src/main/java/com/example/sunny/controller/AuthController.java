package com.example.sunny.controller;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.config.jwt.JwtTokenUtil;
import com.example.sunny.model.User;
import com.example.sunny.service.AuthUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController extends BasicController{
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthUserDetailsService authUserDetailsService;


    @PostMapping("/jwt/refresh")
    public ResponseEntity<Map<String, Object>> refreshJwt(HttpServletRequest request,
                                          @RequestParam(value="refreshToken") String refreshToken) {

        if(refreshToken == null) throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "invalid input - refreshJwt");
        String userId = null;
//        String newRefreshToken = refreshToken;
//        String userIp=request.getRemoteAddr();
//        boolean isExpired = false;
        try {
            userId = jwtTokenUtil.getUsernameFromToken(refreshToken);
        } catch (IllegalArgumentException e) {
            log.info("Unable to get JWT Token");
        } catch (SignatureException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "AuthficationController - refreshJwt");
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
//            isExpired = true;
        }
        UserDetails userDetails = authUserDetailsService.loadUserByUsername(userId);
        String accessToken = jwtTokenUtil.generateToken(userDetails);

        List<HashMap<String, Object>> resultList= new ArrayList<HashMap<String,Object>>();
        HashMap <String,Object> result =new HashMap<>();
        result.put("accessToken",accessToken);
        result.put("refreshToken", refreshToken);
        result.put("userId",userId);
        return createResponse(result);
    }

    @GetMapping("/validation")
    public ResponseEntity<Map<String, Object>> validation(@RequestParam(value="sid") String sid) {
        if(sid == null) throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "invalid input - sid");
//        boolean isExpired=false;
//        String userId="";
        try {
            jwtTokenUtil.getUsernameFromToken(sid);
        }catch( ExpiredJwtException e) { // 토큰 만료?
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }

        return createResponse();
    }

//    @GetMapping("/auth")
//    public Map<String, Object> checkAuth(@RequestParam(value="sid") String sid) {
//        String role = jwtTokenUtil.extractRole(sid);
//        if(!role.equals(XCSMCode.AUTH_ADMIN)) {
//            throw new BusinessException(ErrorCode.SYSTEM_NOT_ALLOWED);
//        }
//        return jsonMap();
//    }


}
