package com.example.sunny.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicController {

    protected ResponseEntity<Map<String, Object>> createResponse(String code, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    protected ResponseEntity<Map<String, Object>> createResponse(Object data) {
        return createResponse("0", data);
    }

    protected ResponseEntity<Map<String, Object>> createResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "0");
        return ResponseEntity.ok(response);
    }
    protected ResponseEntity<Map<String, Object>> createResponseNotOk(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).build();
    }

    protected ResponseEntity<Map<String, Object>> createResponseNotOk(HttpStatus httpStatus, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", String.valueOf(httpStatus.value()));
        response.put("data", data);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
