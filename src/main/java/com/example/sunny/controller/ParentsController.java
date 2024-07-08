package com.example.sunny.controller;

import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/parents")
@Slf4j
public class ParentsController extends BasicController {
    private final ParentsService parentsService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateParents(@RequestBody ParentsDto parentsDto) {
        return createResponse(parentsService.update(parentsDto));
    }

}
