package com.sunny.repository;

import com.sunny.model.Child;

import java.util.List;

public interface ChildRepositoryCustom {
    List<Child> findChildWithBirthMonth(int month);

    List<Child> checkChild(Child child);

    List<Child> findAttendingChildren();

    List<Child> findAllWithRide();

    List<Child> findAllWithParents();
}
