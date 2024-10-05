package com.example.sunny.repository;

import com.example.sunny.model.Child;

import java.util.List;

public interface ChildRepositoryCustom {
    List<Child> findChildWithBirthMonth(int month);

    List<Child> checkChild(Child child);

    List<Child> getAttendingChildren();
}
