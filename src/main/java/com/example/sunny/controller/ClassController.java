package com.example.sunny.controller;

import com.example.sunny.model.dto.SunnyClassDto;
import com.example.sunny.service.SunnyClassServcie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/class")
@Slf4j
public class ClassController extends BasicController {
    private final SunnyClassServcie sunnyClassServcie;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getClasses() {
        return createResponse(sunnyClassServcie.findAll());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addClass(@RequestBody SunnyClassDto sunnyClass) {
        return createResponse(sunnyClassServcie.create(sunnyClass));
    }

    @DeleteMapping
    public  ResponseEntity<Map<String, Object>> deleteClass(@RequestParam(value = "id") Long id) {
        sunnyClassServcie.deleteById(id);
        return createResponse();
    }

}
