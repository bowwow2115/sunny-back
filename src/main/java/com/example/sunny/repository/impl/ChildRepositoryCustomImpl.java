package com.example.sunny.repository.impl;

import com.example.sunny.model.Child;
import com.example.sunny.model.QChild;
import com.example.sunny.repository.ChildRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChildRepositoryCustomImpl implements ChildRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Child> findChildWithBirthMonth(int month) {
        QChild child = QChild.child;
        return queryFactory
                .selectFrom(child)
                .where(child.birthday.month().eq(month))
                .fetch();
    }
}
