package com.sunny.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController extends BasicController {
    @PostMapping
    public ResponseEntity<Map<String, Object>> postNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Notification POST received. subject={}", request.subject());
        return createResponse(request);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> putNotification(@Valid @RequestBody NotificationRequest request) {
        log.info("Notification PUT received. subject={}", request.subject());
        return createResponse(request);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotification(@RequestParam(value = "bodyTest") String body,
                                                               @RequestParam(value = "subjectTest") String subject) {
        NotificationRequest request = new NotificationRequest(subject, body);
        log.info("Notification GET preview requested. subject={}", request.subject());
        return createResponse(request);
    }

    private record NotificationRequest(
            @NotBlank(message = "제목은 필수입니다.")
            String subject,
            @NotBlank(message = "본문은 필수입니다.")
            String body
    ) {
    }
}
