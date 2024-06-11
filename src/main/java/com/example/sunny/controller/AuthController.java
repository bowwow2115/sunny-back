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

    @PostMapping(name = "/login")
    public ResponseEntity<Map<String, Object>> doLogin(@RequestBody User user) {
        Map<String,Object> result =new HashMap();

        try {
            // AuthenticationManager 에게 사용자 입력 정보를 전달한다
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
            UserDetails userDetails = authUserDetailsService.loadUserByUsername(user.getUserId());

            //TODO: 비밀번호 잘못입력 카운트 초기화

            String token = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            result.put("token",token);
            result.put("refreshToken", refreshToken);
            result.put("userId",user.getUserId());
            result.put("role",user.getRole());

        } catch (DisabledException e) {
            throw new BusinessException(ErrorCode.USERDISABLED, "계정이 비활성화 되어있습니다. 관리자에게 문의바랍니다.");
        } catch (UsernameNotFoundException e1) { //계정 없음
            throw new  BusinessException(ErrorCode.WRONG_ID_OR_PASSWORD, "아이디나 비밀번호 값이 올바르지 않습니다.");
        } catch (InternalAuthenticationServiceException e2) { //계정 없음
            throw new  BusinessException(ErrorCode.WRONG_ID_OR_PASSWORD, "아이디나 비밀번호 값이 올바르지 않습니다.");
        } catch (BadCredentialsException e3) { //비밀번호 틀림
//            loginService.updateTrycnt(userId); // trycnt 1 추가
            throw new  BusinessException(ErrorCode.WRONG_ID_OR_PASSWORD, "아이디나 비밀번호 값이 올바르지 않습니다.");
        } catch (AuthenticationCredentialsNotFoundException e4) { //인증 실패
            throw new  BusinessException(ErrorCode.AUTHEXCEPTION, "로그인 할 수 없습니다. 관리자에게 문의바랍니다.");
        } catch (IllegalArgumentException e) {
            throw new  BusinessException(ErrorCode.AUTHEXCEPTION, "로그인 할 수 없습니다. 관리자에게 문의바랍니다.");
        } catch (LockedException e) {
            throw new  BusinessException(ErrorCode.USERDISABLED, "비밀번호 오류로 계정이 잠겼습니다. 관리자에게 문의바랍니다.");
        }
        return createResponse(result);
    }

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
