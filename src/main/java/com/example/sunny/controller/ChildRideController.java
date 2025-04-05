package com.example.sunny.controller;

import com.example.sunny.model.dto.ChilMeetingLocationDto;
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
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody ChilMeetingLocationDto chilMeetingLocationDto) {
        return createResponse(childRideService.update(chilMeetingLocationDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam(value = "id") Long id) {
        childRideService.deleteById(id);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody ChilMeetingLocationDto chilMeetingLocationDto) {
        return createResponse(childRideService.create(chilMeetingLocationDto));
    }

    @PostMapping("/list")
    public ResponseEntity<Map<String, Object>> addChildRideList(@RequestBody List<ChilMeetingLocationDto> chilMeetingLocationDtoList) {
        List<ChilMeetingLocationDto> resultList = new ArrayList<>();
        for (ChilMeetingLocationDto chilMeetingLocationDto : chilMeetingLocationDtoList)
            resultList.add(childRideService.create(chilMeetingLocationDto));
        return createResponse(resultList);
    }
}
