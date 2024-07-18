package com.example.sunny.controller;

import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/child")
@RequiredArgsConstructor
public class ChildController extends BasicController {
    private final ChildService childService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllChildren() {
        return createResponse(childService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getChildByName(@PathVariable("name") String name) {
        return createResponse(childService.findByName(name));
    }

    @GetMapping("/unRided")
    public ResponseEntity<Map<String, Object>> getUnRidedChildren() {
        return createResponse(childService.getUnRidedChildren());
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getChildById(@RequestParam(value = "id") Long id) {
        return createResponse(childService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChild(@RequestBody ChildDto child) {
        return createResponse(childService.create(child));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> editChild(@RequestBody ChildDto child) {
        return addChild(child);
    }
}
