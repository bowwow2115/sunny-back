package com.example.sunny.repository.impl;

import com.example.sunny.code.SunnyCode;
import com.example.sunny.model.*;
import com.example.sunny.repository.ChildRepositoryCustom;
import com.querydsl.core.types.Projections;
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
                .select(Projections.fields(Child.class,
                        qChild.name,
                        qChild.birthday,
                        qChild.className))
                .from(qChild)
                .where(qChild.birthday.month().eq(month))
                .where(qChild.status.eq(SunnyCode.CHILD_STATUS_ATTENDING))
                .fetch();
    }

    @Override
    public List<Child> checkChild(Child child) {
        QChild qChild = QChild.child;
        return queryFactory
                .select(Projections.fields(Child.class, qChild.className, qChild.name))
                .from(qChild)
                .where(qChild.className.eq(child.getClassName()))
                .where(qChild.name.eq(child.getName()))
                .fetch();
    }

    @Override
    public List<Child> findAttendingChildren() {
        QChild qChild = QChild.child;
        return queryFactory
                .select(Projections.fields(Child.class, qChild.id, qChild.name, qChild.className))
                .from(qChild)
                .where(qChild.status.eq(SunnyCode.CHILD_STATUS_ATTENDING))
                .fetch();
    }
    @Override
    public List<Child> findAllWithRide() {
        QChild qChild = QChild.child;
//        return queryFactory
//                .select(Projections.fields(Child.class,
//                        qChild.id,
//                        qChild.name,
//                        qChild.className,
//                        qChild.birthday,
//                        qChild.status,
//                        qChild.address,
//                        qChild.admissionDate))
//                .from(qChild)
//                .leftJoin(qChild.childRideList, QChildRide.childRide).fetchJoin()
//                .leftJoin(QChildRide.childRide.meetingLocation, QMeetingLocation.meetingLocation).fetchJoin()
//                .leftJoin(QMeetingLocation.meetingLocation.sunnyRide, QSunnyRide.sunnyRide).fetchJoin()
//                .fetch();
        return null;
    }

    @Override
    public List<Child> findAllWithParents() {
//        QChild qChild = QChild.child;
//        return queryFactory
//                .select(Projections.fields(Child.class,
//                        qChild.id,
//                        qChild.name,
//                        qChild.className,
//                        qChild.birthday,
//                        qChild.status,
//                        qChild.address,
//                        qChild.admissionDate
////                        JPAExpressions
////                                .select(Projections.fields(Parents.class,
////                                        QParents.parents.name,
////                                        QParents.parents.id,
////                                        QParents.parents.telephone,
////                                        QParents.parents.relation))
////                                .from(QParents.parents)
////                                .where(QParents.parents.child.eq(QChild.child)) // ✅ 서브쿼리 사용
//                        ))
//                .from(qChild)
//                .leftJoin(qChild.parentList, QParents.parents)
//                .fetch();
        return null;
    }


}
