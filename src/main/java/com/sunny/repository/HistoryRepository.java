package com.sunny.repository;

import com.sunny.model.BusinessHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<BusinessHistory, Long>, HistoryRepositoryCustom {
    public List<BusinessHistory> findByCreatedBy(String createdBy);
}
