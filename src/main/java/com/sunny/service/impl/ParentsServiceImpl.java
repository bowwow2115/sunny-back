package com.sunny.service.impl;

import com.sunny.code.Action;
import com.sunny.config.aop.TrackHistory;
import com.sunny.config.error.BusinessException;
import com.sunny.config.error.ErrorCode;
import com.sunny.model.Child;
import com.sunny.model.Parents;
import com.sunny.model.dto.ParentsDto;
import com.sunny.repository.ChildRepository;
import com.sunny.repository.ParentsRepository;
import com.sunny.service.ParentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sunny.code.Action.ADD_PARENTS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParentsServiceImpl implements ParentsService {
    private final ParentsRepository parentsRepository;
    private final ChildRepository childRepository;
    @Override
    @TrackHistory(action = com.sunny.code.Action.FIND_PARENTS_ALL, targetType = Parents.class, noTargetId = true)
    public List<ParentsDto> findAll() {
        return null;
//        return parentsRepository.findAll().stream()
//                .map(ParentsDto::new)
//                .collect(Collectors.toList());
    }

    @Override
    @TrackHistory(action = com.sunny.code.Action.FIND_PARENTS_BYID, targetType = Parents.class, idParamName = "aLong")
    public ParentsDto findById(Long aLong) {
        Parents parents = parentsRepository.findById(aLong).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return new ParentsDto(parents);
    }

    @Override
    @TrackHistory(action = ADD_PARENTS, targetType = Parents.class, idParamName = "object")
    @Transactional
    public ParentsDto create(ParentsDto object) {
        Parents parents = object.toEntity();
        Child child = childRepository.findById(object.getChildId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "등록하려는 원아의 정보가 존재하지 않습니다."));
        parents.addChild(child);
        return new ParentsDto(parentsRepository.save(parents));
    }

    @Override
    @TrackHistory(action = Action.UPDATE_PARENTS, targetType = Parents.class, idParamName = "object")
    @Transactional
    public ParentsDto update(ParentsDto object) {
        return new ParentsDto(parentsRepository.save(object.toEntity()));
    }

    @Override
    @TrackHistory(action = Action.DELETE_PARENTS, targetType = Parents.class, idParamName = "object")
    @Transactional
    public void delete(ParentsDto object) {
        parentsRepository.delete(object.toEntity());
    }

    @Override
    @TrackHistory(action = Action.DELETE_PARENTS_BYID, targetType = Parents.class, idParamName = "aLong")
    @Transactional
    public void deleteById(Long aLong) {
        parentsRepository.deleteById(aLong);
    }

    @Override
    @TrackHistory(action = Action.FIND_PARENTS_BYNAME, targetType = Parents.class)
    public List<ParentsDto> findByName(String name) {
        List<Parents> parentsList = parentsRepository.findByName(name);
        if(parentsList == null) parentsList = new ArrayList<>();
        return parentsList.stream().map(ParentsDto::new).collect(Collectors.toList());
    }
}
