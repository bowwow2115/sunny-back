package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.ChildRide;
import com.example.sunny.model.SunnyRide;
import com.example.sunny.model.dto.ChildDto;
import com.example.sunny.model.dto.ParentsDto;
import com.example.sunny.model.dto.ChildRideDto;
import com.example.sunny.model.dto.SunnyRideDto;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.ParentsRepository;
import com.example.sunny.repository.ChildRideRepository;
import com.example.sunny.repository.SunnyRideRepository;
import com.example.sunny.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final SunnyRideRepository sunnyRideRepository;
    private final ChildRideRepository childRideRepository;

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
            for (ParentsDto parentsDto : object.getParentList()) {
                child.addParents(parentsDto.toEntity());
            }
        }
        //등록된 차량인지 확인
        SunnyRide amRide = null;
        SunnyRide pmRide = null;
        if (object.getAmRide() != null) {
            amRide = sunnyRideRepository.findById(object.getAmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            ChildRide childRide = new ChildRide();
            childRide.setSunnyRide(amRide);
            childRide.setTime(object.getAmRide().getTime());
            childRide.setComment(object.getAmRide().getComment());
            child.addRide(childRide);
        }
        if (object.getPmRide() != null) {
            pmRide = sunnyRideRepository.findById(object.getPmRide().getSunnyRide().getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
            ChildRide childRide = new ChildRide();
            childRide.setSunnyRide(pmRide);
            childRide.setTime(object.getPmRide().getTime());
            childRide.setComment(object.getPmRide().getComment());
            child.addRide(childRide);
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

        if(result.getChildRideList() != null && result.getChildRideList().size() != 0) {
            result.getChildRideList().stream()
                    .filter((item) -> item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        childDto.setAmRide(ChildRideDto.builder()
                                .comment(item.getComment())
                                .sunnyRide(new SunnyRideDto(item.getSunnyRide()))
                                .time(item.getTime())
                                .id(item.getId())
                                .build()
                        );
                    });

            result.getChildRideList().stream()
                    .filter((item) -> !item.getSunnyRide().isAm())
                    .findAny()
                    .ifPresent((item) -> {
                        childDto.setPmRide(ChildRideDto.builder()
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
