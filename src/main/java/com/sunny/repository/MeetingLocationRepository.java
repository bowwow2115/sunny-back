package com.sunny.repository;

import com.sunny.model.MeetingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingLocationRepository extends JpaRepository<MeetingLocation, Long> {
}
