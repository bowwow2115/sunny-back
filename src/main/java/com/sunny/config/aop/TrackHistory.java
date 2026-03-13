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
    String idParamName() default "id"; // 파라미터 중 ID 로 사용할 키 이름
}