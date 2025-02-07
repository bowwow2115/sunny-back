package com.example.sunny.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController extends BasicController {
    @PostMapping
    public ResponseEntity<Map<String, Object>> getNotification(@RequestBody Map<String, String> jsonObject) {
        System.out.println(jsonObject);
        return createResponse();
    }
}
