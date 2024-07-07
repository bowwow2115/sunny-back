package com.example.sunny.controller;

import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.service.ChildRideService;
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
    private final ChildRideService childRideService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody ChildRideDto childRideDto) {
        return createResponse(childRideService.update(childRideDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam Long along) {
        childRideService.deleteById(along);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody ChildRideDto childRideDto) {
        return createResponse(childRideService.create(childRideDto));
    }

}
