package com.sunny.controller;

import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.config.jwt.JwtTokenUtil;
import com.sunny.model.dto.JwtDto;
import com.sunny.service.AuthUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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
                                          @RequestBody JwtDto jwtDto) {
        if(jwtDto.getRefreshToken() == null) throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "invalid input - refreshJwt");
        String userId = null;
        try {
            userId = jwtTokenUtil.getUsernameFromToken(jwtDto.getRefreshToken());
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
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

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
        String userId="";
        try {
            userId = jwtTokenUtil.getUsernameFromToken(sid);
        }catch( ExpiredJwtException e) { // 토큰 만료?
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        }
        Object extractRole = jwtTokenUtil.extractRole(sid);
        List<Map<String, String>> rtnList = new ArrayList<>();
        if (extractRole instanceof List) {
            rtnList = (List) extractRole;
            Map<String, String> map = new HashMap<>();
            map.put("userId", userId);
            rtnList.add(map);
            return createResponse(rtnList);
        }
        return createResponse(jwtTokenUtil.extractRole(sid));
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
