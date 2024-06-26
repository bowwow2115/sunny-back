package com.example.sunny.controller;

import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.service.ChildParentsService;
import com.example.sunny.service.ChildService;
import com.example.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/child")
@RequiredArgsConstructor
public class ChildController extends BasicController {

    private final ChildService childService;
    private final ParentsService parentsService;
    private final ChildParentsService childParentsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllChildren() {
        return createResponse(childService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getChildByName(@PathVariable("name") String name) {
        return createResponse(childService.findByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getChildById(@PathVariable("id") Long id) {
        return createResponse(childService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChild(@RequestBody ChildDto child) {
        ChildDto childResult = childService.create(child);
        ParentsDto parentResult = null;
        for(ParentsDto parentsDto : child.getParentList()) {
            parentResult = parentsService.create(parentsDto);
        }

        return createResponse(result);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> editChild(@RequestBody ChildDto child) {
        return addChild(child);
    }
}
