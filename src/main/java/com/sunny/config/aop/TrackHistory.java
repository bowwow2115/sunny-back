package com.sunny.config.aop;

import com.sunny.code.Action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service 단에서 사용해야 함
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackHistory {
    Action action();
    Class<?> targetType();    // 대상 타입 (예: "Child")
    boolean idFromReturn() default true;
    boolean noTargetId() default false; //타겟아이디를 따로 등록시키길 원하지 않는 경우 true
    String idParamName() default "aLong"; // 파라미터 중 ID를 직간접적으로 가지고있는 것의 이름
}