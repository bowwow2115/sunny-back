package com.sunny.controller;

import com.sunny.model.dto.HistorySearchCondition;
import com.sunny.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/history")
@Slf4j
public class HistoryController extends BasicController {
    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getRecentHistory(
            @PageableDefault(page = 0, size = 100) Pageable pageable,
            HistorySearchCondition historySearchCondition) {
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort());
        }
        return createResponse(historyService.getHistoryByCondition(pageable, historySearchCondition));
    }
}
