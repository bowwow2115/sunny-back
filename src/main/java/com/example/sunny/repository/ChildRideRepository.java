package com.example.sunny.repository;

import com.example.sunny.model.ChildMeetingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRideRepository extends JpaRepository<ChildMeetingLocation, Long> {
}
