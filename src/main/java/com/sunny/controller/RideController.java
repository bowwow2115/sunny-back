package com.sunny.controller;

import com.sunny.model.dto.SunnyRideDto;
import com.sunny.service.SunnyRideService;
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

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getRides() {
        return createResponse(sunnyRideService.findAll());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addRide(@RequestBody SunnyRideDto sunnyRideDto) {
        return createResponse(sunnyRideService.create(sunnyRideDto));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateRide(@RequestBody SunnyRideDto sunnyRideDto) {
        return createResponse(sunnyRideService.update(sunnyRideDto));
    }

    @DeleteMapping
    public  ResponseEntity<Map<String, Object>> deleteRide(@RequestParam Long id) {
        sunnyRideService.deleteById(id);
        return createResponse();
    }
}
