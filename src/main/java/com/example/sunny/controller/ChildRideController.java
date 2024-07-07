package com.example.sunny.controller;

import com.example.sunny.model.dto.SunnyChildRideDto;
import com.example.sunny.service.SunnyChildRideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/childRide")
@RequiredArgsConstructor
@Slf4j
public class ChildRideController extends BasicController {
    private final SunnyChildRideService sunnyChildRideService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody SunnyChildRideDto sunnyChildRideDto) {
        return createResponse(sunnyChildRideService.update(sunnyChildRideDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam Long along) {
        sunnyChildRideService.deleteById(along);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody SunnyChildRideDto sunnyChildRideDto) {
        return createResponse(sunnyChildRideService.create(sunnyChildRideDto));
    }

}
