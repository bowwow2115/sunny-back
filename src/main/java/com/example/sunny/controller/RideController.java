package com.example.sunny.controller;

import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.service.SunnyRideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ride")
@Slf4j
public class RideController extends BasicController{
    private final SunnyRideService sunnyRideService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getRides() {
        return createResponse(sunnyRideService.findAll());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addRide(@RequestBody SunnyRideDto sunnyRideDto) {
        return createResponse(sunnyRideService.create(sunnyRideDto));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateRide(@RequestBody SunnyRideDto sunnyRideDto) {
        return createResponse(sunnyRideService.create(sunnyRideDto));
    }

    @DeleteMapping
    public  ResponseEntity<Map<String, Object>> deelteRide(@RequestBody SunnyRideDto sunnyClass) {
        sunnyRideService.delete(sunnyClass);
        return createResponse();
    }
}
