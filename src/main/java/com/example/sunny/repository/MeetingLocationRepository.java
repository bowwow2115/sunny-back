package com.example.sunny.repository;

import com.example.sunny.model.MeetingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingLocationRepository extends JpaRepository<MeetingLocation, Long> {
}
