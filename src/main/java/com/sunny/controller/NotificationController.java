package com.sunny.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/notification")
public class NotificationController extends BasicController {
    @PostMapping
    public ResponseEntity<Map<String, Object>> postNotification(@RequestBody Map<String, String> jsonObject) {
        System.out.println(jsonObject);
        return createResponse();
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> putNotification(@RequestBody Map<String, String> jsonObject) {
        System.out.println(jsonObject);
        return createResponse();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotification(@RequestParam(value = "bodyTest") String body,
                                                               @RequestParam(value = "subjectTest") String subject,
                                                               @RequestHeader("Authorization") String authHeader) {
        if (authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
        }
        return createResponse();
    }
}
