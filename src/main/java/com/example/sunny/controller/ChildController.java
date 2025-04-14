package com.example.sunny.controller;

import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("/allWithRide")
    public ResponseEntity<Map<String, Object>> getAllWithRide() {
        return createResponse(childService.findAllWithRide());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getChildByName(@PathVariable("name") String name) {
        return createResponse(childService.findByName(name));
    }

    @GetMapping("/checkChild")
    public ResponseEntity<Map<String, Object>> checkChild(@RequestParam(value = "name") String name,
                                                          @RequestParam(value = "className") String className) {
        ChildDto childDto = ChildDto.builder().name(name).className(className).build();
        List<ChildDto> childDtoList = childService.checkChild(childDto);
//        if(childDtoList.size() > 0) throw new BusinessException(ErrorCode.DATAALREDAYEXISTS, "해당 원아가 이미 존재합니다.");
        if(childDtoList.size() > 0) return createResponse(childDtoList);
        else return createResponse();
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
        return createResponse(childService.update(child));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChild(@RequestParam Long id) {
        childService.deleteById(id);
        return createResponse();
    }

    @GetMapping("/birth")
    public ResponseEntity<Map<String, Object>> getBirthMonthChlid() {
        int currentMonth = LocalDate.now().getMonthValue();
        return createResponse(childService.findChildWithBirthMonth(currentMonth));
    }

    @GetMapping("/all/attending")
    public ResponseEntity<Map<String, Object>> getAttendingChildren() {
        return createResponse(childService.getAttendingChildren());
    }

    @PutMapping("/all/class")
    public ResponseEntity<Map<String, Object>> updateChildrenClass(@RequestBody List<ChildDto> childDtoList,
                                                                   @RequestParam(value = "className") String className) {
        return createResponse(childService.updateChildrenClass(childDtoList, className));
    }
}
