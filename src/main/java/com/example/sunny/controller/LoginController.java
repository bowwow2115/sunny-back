package com.example.sunny.controller;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.config.jwt.JwtTokenUtil;
import com.example.sunny.model.User;
import com.example.sunny.service.AuthUserDetailsService;
import com.example.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController extends BasicController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthUserDetailsService authUserDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doLogin(@RequestBody User user) {
        Map<String,Object> result =new HashMap();
        try {
            // AuthenticationManager 에게 사용자 입력 정보를 전달한다
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
            UserDetails userDetails = authUserDetailsService.loadUserByUsername(user.getUserId());

            String token = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            result.put("accessToken", token);
            result.put("refreshToken", refreshToken);
            result.put("userId", userDetails.getUsername());
            result.put("roles", userDetails.getAuthorities());
        } catch (
            DisabledException e) {
            throw new BusinessException(ErrorCode.USERDISABLED, "계정이 비활성화 되어있습니다. 관리자에게 문의바랍니다.");
        } catch (
            UsernameNotFoundException e1) { //계정 없음
            throw new  BusinessException(ErrorCode.WRONG_ID_OR_PASSWORD, "아이디나 비밀번호 값이 올바르지 않습니다.");
        } catch (
            InternalAuthenticationServiceException e2) { //계정 없음
            throw new  BusinessException(ErrorCode.WRONG_ID_OR_PASSWORD, "아이디나 비밀번호 값이 올바르지 않습니다.");
        } catch (
            BadCredentialsException e3) { //비밀번호 틀림
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

//    @PostMapping("/user")
//    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserDto userDto) {
//        if(StringUtil.isEmpty(userDto.getUserId()) || StringUtil.isEmpty(userDto.getPassword()) || StringUtil.isEmpty(userDto.getUserName())
//                || StringUtil.isEmpty(userDto.getTelephone()) || StringUtil.isEmpty(userDto.getEmail()))
//            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
//        return createResponse(userService.create(userDto));
//    }

}
