package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.Parents;
import com.example.sunny.model.SunnyChildRide;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.model.dto.SunnyChildRideDto;
import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.repository.SunnyChildRideRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final SunnyRideRepository sunnyRideRepository;
    private final SunnyChildRideRepository sunnyChildRideRepository;

    @Override
    public ChildDto findByName(String name) {
        Child result = childRepository.findByname(name);
        ChildDto childDto = new ChildDto(result);
        return addJoinData(childDto, result);
    }

    @Override
    public List<ChildDto> findAll() {
        return childRepository.findAll().stream()
                .map((result)-> {
                    ChildDto childDto = new ChildDto(result);
                    return addJoinData(childDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChildDto findById(Long aLong) {
        Child result = childRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "해당 원아의 정보가 존재하지 않습니다."));
        ChildDto childDto = new ChildDto(result);
        return addJoinData(childDto, result);
    }
    @Transactional
    @Override
    public ChildDto create(ChildDto object) {

        Child child = object.toEntity();
        //부모정보 존재 확인 후 등록
        if(object.getParentList().size() != 0) {
            List<Parents> parentsList = new ArrayList<>();
            for (ParentsDto parentsDto : object.getParentList()) {
                parentsList.add(parentsDto.toEntity());
            }
            child.setParentList(parentsList);
        }
        //등록된 차량인지 확인
        SunnyRide amRide = null;
        SunnyRide pmRide = null;
        if (object.getAmRide() != null) {
            amRide = sunnyRideRepository.findById(object.getAmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            SunnyChildRide sunnyChildRide = new SunnyChildRide();
            sunnyChildRide.setSunnyRide(amRide);
            sunnyChildRide.setTime(object.getAmRide().getTime());
            sunnyChildRide.setComment(object.getAmRide().getComment());
            child.addRide(sunnyChildRide);
        }
        if (object.getPmRide() != null) {
            pmRide = sunnyRideRepository.findById(object.getPmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            SunnyChildRide sunnyChildRide = new SunnyChildRide();
            sunnyChildRide.setSunnyRide(pmRide);
            sunnyChildRide.setTime(object.getPmRide().getTime());
            sunnyChildRide.setComment(object.getPmRide().getComment());
            child.addRide(sunnyChildRide);
        }

        Child result = childRepository.save(child);
        ChildDto childDto = new ChildDto(result);
        return addJoinData(childDto, result);
    }

    @Override
    public ChildDto update(ChildDto object) {
        Child child = object.toEntity();
        return new ChildDto(childRepository.save(child));
    }

    @Override
    public void delete(ChildDto object) {
        Child child = object.toEntity();
        if (object.getParentList().size()!=0)
            child.setParentList(object.getParentList().stream()
                .map(ParentsDto::toEntity).collect(Collectors.toList()));

        childRepository.delete(child);
    }

    @Override
    public void deleteById(Long aLong) {
        childRepository.deleteById(aLong);
    }

    private ChildDto addJoinData(ChildDto childDto, Child result) {
        if(result.getParentList() != null && result.getParentList().size() != 0)
            childDto.setParentList(result.getParentList().stream().map(ParentsDto::new)
                    .collect(Collectors.toList()));

        if(result.getSunnyChildRideList() != null && result.getSunnyChildRideList().size() != 0) {
            result.getSunnyChildRideList().stream()
                    .filter((item) -> item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        childDto.setAmRide(SunnyChildRideDto.builder()
                                .comment(item.getComment())
                                .sunnyRide(new SunnyRideDto(item.getSunnyRide()))
                                .time(item.getTime())
                                .id(item.getId())
                                .build()
                        );
                    });

            result.getSunnyChildRideList().stream()
                    .filter((item) -> !item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        childDto.setPmRide(SunnyChildRideDto.builder()
                                .comment(item.getComment())
                                .sunnyRide(new SunnyRideDto(item.getSunnyRide()))
                                .time(item.getTime())
                                .id(item.getId())
                                .build()
                        );
                    });
        }
        return childDto;
    }

}
