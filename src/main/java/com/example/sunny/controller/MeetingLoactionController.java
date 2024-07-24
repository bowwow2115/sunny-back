package com.example.sunny.controller;

import com.example.sunny.model.dto.MeetingLocationDto;
import com.example.sunny.service.MeetingLoactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/meetingLocation")
@RequiredArgsConstructor
public class MeetingLoactionController extends BasicController{
    private final MeetingLoactionService meetingLoactionService;
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateMeetingLoaction(@RequestBody MeetingLocationDto meetingLocation) {
       return createResponse(meetingLoactionService.update(meetingLocation));
    }
}
