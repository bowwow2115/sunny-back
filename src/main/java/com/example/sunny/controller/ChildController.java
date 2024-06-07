package com.example.sunny.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/child")
public class ChildController extends BasicController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllChildren() {
        return createResponse();
    }
}
