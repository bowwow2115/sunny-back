package com.example.sunny.controller;

import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.service.ChildRideService;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/childRide")
@RequiredArgsConstructor
@Slf4j
public class ChildRideController extends BasicController {
    private final ChildRideService childRideService;
    private final ChildService childService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody ChildRideDto childRideDto) {
        return createResponse(childRideService.update(childRideDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam(value = "id") Long id) {
        childRideService.deleteById(id);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody ChildRideDto childRideDto) {
        return createResponse(childRideService.create(childRideDto));
    }

    @PostMapping("/list")
    public ResponseEntity<Map<String, Object>> addChildRideList(@RequestBody List<ChildRideDto> childRideDtoList) {
        List<ChildRideDto> resultList = new ArrayList<>();
        for (ChildRideDto childRideDto : childRideDtoList)
            resultList.add(childRideService.create(childRideDto));
        return createResponse(resultList);
    }
}
