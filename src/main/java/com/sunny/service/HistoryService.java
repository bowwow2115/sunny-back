package com.sunny.service;

import com.sunny.model.BusinessHistory;
import org.springframework.scheduling.annotation.Async;

public interface HistoryService extends CrudService<BusinessHistory, Long> {
    @Async
    public void asyncCreate(BusinessHistory object);


}
