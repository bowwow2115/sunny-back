package com.example.sunny.controller;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.service.ChildRideService;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/childRide")
@RequiredArgsConstructor
@Slf4j
public class ChildRideController extends BasicController {
    private final ChildRideService childRideService;
    private final ChildService childService;

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateChildRide(@RequestBody ChildRideDto childRideDto) {
        return createResponse(childRideService.update(childRideDto));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteChildRideById(@RequestParam(value = "id") Long id) {
        childRideService.deleteById(id);
        return createResponse();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addChildRide(@RequestBody ChildRideDto childRideDto) {
        ChildDto childDto = childService.findById(childRideDto.getChild().getId());
        //차량정보 존재확인
        if(childDto.getAmRide() != null && childRideDto.getSunnyRide() != null) {
            if(childRideDto.getSunnyRide().isAm()) throw new BusinessException(ErrorCode.DATAALREDAYEXISTS, "차량정보가 이미 등록되어있습니다.");
        } else if(childDto.getPmRide() != null && childRideDto.getSunnyRide() != null) {
            if(!childRideDto.getSunnyRide().isAm()) throw new BusinessException(ErrorCode.DATAALREDAYEXISTS, "차량정보가 이미 등록되어있습니다.");
        }
        return createResponse(childRideService.create(childRideDto));
    }

}
