package com.example.sunny.service.impl;

import com.example.sunny.config.error.BusinessException;
import com.example.sunny.config.error.ErrorCode;
import com.example.sunny.model.Child;
import com.example.sunny.model.ChildMeetingLocation;
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
        return addChildRide(childDto, result);
    }

    @Override
    public List<ChildDto> findChildWithBirthMonth(int month) {
        return childRepository.findChildWithBirthMonth(month).stream()
                .map((result) -> new ChildDto(result))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> checkChild(ChildDto object) {
        return childRepository.checkChild(object.toEntity()).stream()
                .map(result -> new ChildDto(result))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> getAttendingChildren() {
        return childRepository.findAttendingChildren().stream()
                .map((result)-> new ChildDto(result))
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
            Child result = childRepository.save(child);
            childDtoList.add(addJoinData(new ChildDto(child), result));
        });
        return childDtoList;
    }

    @Override
    public List<ChildDto> findAll() {
        return childRepository.findAllWithParents().stream()
                .map((result)-> {
                    ChildDto childDto = new ChildDto(result);
                    return addParents(childDto, result);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> findAllWithRide() {
        return childRepository.findAllWithRide().stream()
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
                        ChildMeetingLocation childMeetingLocation = new ChildMeetingLocation();
                                childMeetingLocation.updateMeetingLocation(meetingLocation);
                                childMeetingLocation.updateComment(item.getComment());
                                child.addRide(childMeetingLocation);
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
        child.updateChildRideList(origin.getChildMeetingLocations());
        child.updateParentList(origin.getParents());
        return new ChildDto(childRepository.save(child));
    }

    @Override
    public void delete(ChildDto object) {
        Child child = object.toEntity();
        childRepository.delete(child);
    }

    @Override
    public void deleteById(Long aLong) {
        childRepository.deleteById(aLong);
    }

    private ChildDto addParents(ChildDto childDto, Child result) {
        if(result.getParents() != null && result.getParents().size() != 0)
            childDto.setParentList(result.getParents().stream().map(ParentsDto::new)
                    .collect(Collectors.toList()));
        return childDto;
    }

    private ChildDto addChildRide(ChildDto childDto, Child result) {
        if(result.getChildMeetingLocations() != null && result.getChildMeetingLocations().size() != 0) {
            List<ChilMeetingLocationDto> chilMeetingLocationDtoList = result.getChildMeetingLocations().stream()
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
                        ChilMeetingLocationDto chilMeetingLocationDto = new ChilMeetingLocationDto(item);
                        chilMeetingLocationDto.setMeetingLocation(meetingLocationDto);
                        return chilMeetingLocationDto;
                    })
                    .collect(Collectors.toList());
            childDto.setChildRideList(chilMeetingLocationDtoList);
        }
        return childDto;
    }

    private ChildDto addJoinData(ChildDto childDto, Child result) {
        ChildDto addParentsResult = addParents(childDto, result);
        return addChildRide(addParentsResult, result);
    }

}
