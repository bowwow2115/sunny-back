package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.ChildRide;
import com.example.sunny.model.MeetingLocation;
import com.example.sunny.model.dto.*;
import com.example.sunny.repository.ChildRepository;
import com.example.sunny.repository.MeetingLoactionRepository;
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
    private final MeetingLoactionRepository meetingLoactionRepository;

    @Override
    public ChildDto findByName(String name) {
        Child result = childRepository.findByName(name);
        ChildDto childDto = new ChildDto(result);
        return addJoinData(childDto, result);
    }

    @Override
    public List<ChildDto> findChildWithBirthMonth(int month) {
        return childRepository.findChildWithBirthMonth(month).stream()
                .map((result) -> {
                    ChildDto childDto = new ChildDto(result);
                    return addJoinData(childDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> checkChild(ChildDto object) {
        return childRepository.checkChild(object.toEntity()).stream()
                .map((result) -> {
                    ChildDto childDto = new ChildDto(result);
                    return addJoinData(childDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> getAttendingChildren() {
        return childRepository.getAttendingChildren().stream()
                .map((result)-> {
                    ChildDto childDto = new ChildDto(result);
                    return addJoinData(childDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ChildDto> updateChildrenClass(List<ChildDto> childrenList, String className) {
        List<Child> childList = new ArrayList<>();
        childrenList.forEach((child) ->
                childList.add(childRepository.findById(child.getId()).orElseThrow(
                    () -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "해당 원아의 정보가 존재하지 않습니다."))));
        List<ChildDto> childDtoList = new ArrayList<>();
        childList.forEach((child) -> {
            child.updateClassName(className);
            childRepository.save(child);
            childDtoList.add(addJoinData(new ChildDto(child), child));
        });

        return childDtoList;
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
        if(object.getChildRideList() != null)
            object.getChildRideList().stream()
                    .forEach((item) -> {
                        MeetingLocation meetingLocation = meetingLoactionRepository.findById(item.getMeetingLocation().getId()).orElseThrow(
                                () -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "선택한 차량이 존재하지 않습니다."));
                        ChildRide childRide = new ChildRide();
                                childRide.updateMeetingLocation(meetingLocation);
                                childRide.updateComment(item.getComment());
                                child.addRide(childRide);
                            });

        Child result = childRepository.save(child);
        ChildDto childDto = new ChildDto(result);
        return addJoinData(childDto, result);
    }

    @Override
    public ChildDto update(ChildDto object) {
        Child child = object.toEntity();
        Child origin = childRepository.findById(object.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "업데이트할 원아가 존재하지 않습니다."));
        child.updateChildRideList(origin.getChildRideList());
        child.updateParentList(origin.getParentList());
        return new ChildDto(childRepository.save(child));
    }

    @Override
    public void delete(ChildDto object) {
        Child child = object.toEntity();
        if (object.getParentList().size()!=0)
            child.updateParentList(object.getParentList().stream()
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
            List<ChildRideDto> childRideDtoList = result.getChildRideList().stream()
                    .map((item) -> {
                        //
                        MeetingLocationDto meetingLocationDto = new MeetingLocationDto(item.getMeetingLocation());
                        
                        //result에서 하위 값들 꺼낸 후 입력
                        SunnyRideDto sunnyRideDto = new SunnyRideDto(item.getMeetingLocation().getSunnyRide());
                        List<MeetingLocation> meetingLocationList = item.getMeetingLocation().getSunnyRide().getMeetingLocationList();
                        List<MeetingLocationDto> meetingLocationDtoList = meetingLocationList.stream()
                                .map((meetingLocation) -> new MeetingLocationDto(meetingLocation))
                                .collect(Collectors.toList());
                        sunnyRideDto.setMeetingLocationList(meetingLocationDtoList);
                        
                        meetingLocationDto.setSunnyRide(sunnyRideDto);
                        ChildRideDto childRideDto = new ChildRideDto(item);
                        childRideDto.setMeetingLocation(meetingLocationDto);
                        return childRideDto;
                    })
                    .collect(Collectors.toList());
            childDto.setChildRideList(childRideDtoList);
        }
        return childDto;
    }

}
