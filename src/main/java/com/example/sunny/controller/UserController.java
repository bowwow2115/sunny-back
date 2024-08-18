package com.example.sunny.controller;

import com.example.sunny.model.dto.UserDto;
import com.example.sunny.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BasicController {
    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return createResponse(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserDto userDto) {
        return createResponse(userService.create(userDto));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserDto userDto) {
        return createResponse(userService.create(userDto));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam Long id) {
        userService.deleteById(id);
        return createResponse();
    }
}
