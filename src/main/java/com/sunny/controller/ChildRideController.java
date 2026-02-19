package com.sunny.controller;

import com.sunny.model.dto.ChilMeetingLocationDto;
import com.sunny.service.ChildMeetingLocationService;
import com.sunny.service.ChildService;
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
    private final ChildMeetingLocationService childMeetingLocationService;
    private final ChildService childService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody ChilMeetingLocationDto chilMeetingLocationDto) {
        return createResponse(childMeetingLocationService.update(chilMeetingLocationDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam(value = "id") Long id) {
        childMeetingLocationService.deleteById(id);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody ChilMeetingLocationDto chilMeetingLocationDto) {
        return createResponse(childMeetingLocationService.create(chilMeetingLocationDto));
    }

    @PostMapping("/list")
    public ResponseEntity<Map<String, Object>> addChildRideList(@RequestBody List<ChilMeetingLocationDto> chilMeetingLocationDtoList) {
        List<ChilMeetingLocationDto> resultList = new ArrayList<>();
        for (ChilMeetingLocationDto chilMeetingLocationDto : chilMeetingLocationDtoList)
            resultList.add(childMeetingLocationService.create(chilMeetingLocationDto));
        return createResponse(resultList);
    }
}
