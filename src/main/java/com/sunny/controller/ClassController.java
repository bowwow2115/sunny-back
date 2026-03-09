package com.sunny.controller;

import com.sunny.model.dto.SunnyClassDto;
import com.sunny.service.SunnyClassService;
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
    private final SunnyClassService sunnyClassService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getClasses() {
        return createResponse(sunnyClassService.findAll());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addClass(@RequestBody SunnyClassDto sunnyClass) {
        return createResponse(sunnyClassService.create(sunnyClass));
    }

    @DeleteMapping
    public  ResponseEntity<Map<String, Object>> deleteClass(@RequestParam(value = "id") Long id) {
        sunnyClassService.deleteById(id);
        return createResponse();
    }

}
