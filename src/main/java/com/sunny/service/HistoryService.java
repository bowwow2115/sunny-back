package com.sunny.service;

import com.sunny.model.BusinessHistory;

public interface HistoryService extends CrudService<BusinessHistory, Long> {
    public void save(BusinessHistory businessHistory);

}
