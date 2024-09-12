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
        QChild qChild = QChild.child;
        return queryFactory
                .selectFrom(qChild)
                .where(qChild.birthday.month().eq(month))
                .fetch();
    }

    @Override
    public List<Child> checkChild(Child child) {
        QChild qChild = QChild.child;
        return queryFactory
                .selectFrom(qChild)
                .where(qChild.className.eq(child.getClassName()))
                .where(qChild.name.eq(child.getName()))
                .fetch();
    }
}
