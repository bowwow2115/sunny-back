package com.sunny.controller;

import com.sunny.model.dto.MeetingLocationDto;
import com.sunny.service.MeetingLoactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/meetingLocation")
@RequiredArgsConstructor
public class MeetingLocationController extends BasicController{
    private final MeetingLoactionService meetingLoactionService;
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateMeetingLoaction(@RequestBody MeetingLocationDto meetingLocation) {
       return createResponse(meetingLoactionService.update(meetingLocation));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addMeetingLoaction(@RequestBody MeetingLocationDto meetingLocation) {
        return createResponse(meetingLoactionService.create(meetingLocation));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteMeetingLoaction(@RequestParam Long id) {
        meetingLoactionService.deleteById(id);
        return createResponse();
    }}
