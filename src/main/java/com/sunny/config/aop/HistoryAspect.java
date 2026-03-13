package com.sunny.config.aop;

import com.sunny.model.BusinessHistory;
import com.sunny.service.HistoryService;
import com.sunny.util.JsonUtils;
import com.sunny.util.RequestUtil;
import com.sunny.util.UserContextUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class HistoryAspect {
    private final HistoryService historyService;
    // Spring Security 등을 통해 현재 사용자 정보를 가져오는 유틸
    private final UserContextUtil userContextUtil;
    private final RequestUtil requestUtil;
    private final EntityManager entityManager;
    private static final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(trackHistory)")
    public Object logHistory(ProceedingJoinPoint joinPoint, TrackHistory trackHistory) throws Throwable {

        // 1. [Before] 파라미터에서 ID 시도 (수정/삭제용)
        Long targetId = extractIdFromArgs(joinPoint, trackHistory.idParamName());
        Object oldValue = null;

        // 2. 파라미터에 ID 가 있다면 (수정/삭제 케이스)
        if (targetId != null) {
            oldValue = entityManager.find(trackHistory.targetType(), targetId);
        }

        // 3. 비즈니스 로직 실행
        Object result = joinPoint.proceed();

        // 4. ID 재확인 (생성 케이스용)
        if (targetId == null && trackHistory.idFromReturn() && result != null) {
            targetId = extractIdFromObject(result, trackHistory.targetType());
        }

        // 5. 새 값 추출
        Object newValue = null;
        if (targetId != null) {
            // 반환값이 엔티티면 그것을 사용, 아니면 DB 에서 조회
            if (trackHistory.targetType().isInstance(result)) {
                newValue = result;
            } else {
                newValue = entityManager.find(trackHistory.targetType(), targetId);
            }
        } else {
            // ID 를 끝까지 못 찾았으면 반환값 전체를 새 값으로 간주 (DTO 등)
            newValue = result;
        }

        // 6. 이력 객체 생성 및 저장
        BusinessHistory history = createHistory(targetId, trackHistory, oldValue, newValue);

        historyService.save(history);

//        transactionUtil.registerSynchronizationSafe(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                // 메인 트랜잭션 커밋 후에 비동기로 이력 저장
//                historyService.save(history);
//            }
//        });

        return result;
    }

    private BusinessHistory createHistory(Long targetId, TrackHistory trackHistory, Object oldValue, Object newValue) {
        String userId = userContextUtil.getCurrentUserId();
        String userName = userContextUtil.getCurrentUserName();
        String url = requestUtil.getRequestURI();
        String ip = requestUtil.getClientIp();
        String method = requestUtil.getHttpMethod();
        String targetType = trackHistory.targetType().getSimpleName();

        return BusinessHistory.builder()
                .targetType(targetType)
                .url(url)
                .ip(ip)
                .method(method)
                .targetId(targetId)
                .createdBy(userId)
                .content(userName)
                .changedValue(JsonUtils.toDiffMap(oldValue, newValue))
                .build();
    }

    /**
     * 파라미터 배열에서 ID 값 추출
     */
    private Long extractTargetId(Object[] args, String idFieldName) {
        if (args == null || args.length == 0) {
            return null;
        }

        // 간단히 첫 번째 Long 타입 파라미터를 ID 로 간주
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }

        // 또는 파라미터 이름으로 찾기 (컴파일 옵션 -parameters 필요)
        // ParameterNameDiscoverer 등을 활용 가능
        return null;
    }

    private Long extractIdFromObject(Object obj, Class clazz) {
        if (obj == null) return null;

        try {
            // 1. 필드에서 @Id 탐색 (상속 포함)
            Field idField = findIdField(clazz);
            if (idField != null) {
                //dto 타입에서 값 추출
                String idFieldName = idField.getName();
                Field dtoIdField = obj.getClass().getDeclaredField(idFieldName);
                dtoIdField.setAccessible(true);
                return getIdValue(dtoIdField.get(obj));
            }

        } catch (Exception e) {
            log.warn("ID 추출 실패: {}", e.getMessage());
        }
        return null;
    }

    private Class<?> getEntityClass(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.getName().contains("$HibernateProxy") ||
                clazz.getName().contains("$EnhancerBySpringCGLIB")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    private Method findIdMethod(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) return null;

        Method idMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Id.class))
                .filter(m -> m.getParameterCount() == 0)
                .findFirst()
                .orElse(null);

        if (idMethod != null) return idMethod;

        return findIdMethod(clazz.getSuperclass());
    }

    private Long getIdValue(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) return Long.valueOf((String) value);
        return null;
    }

    private Field findIdField(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) {
            return null;
        }

        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);

        if (idField != null) {
            return idField;
        }

        // 2. 없으면 부모 클래스로 재귀 탐색
        return findIdField(clazz.getSuperclass());
    }


    /**
     * JoinPoint 에서 실제 메서드 객체 추출
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            return ((MethodSignature) signature).getMethod();
        }
        return null;
    }

    public Long extractIdFromArgs(ProceedingJoinPoint joinPoint, String idParamName) {
        Method method = getMethod(joinPoint);
        if (method == null) {
            log.warn("Method not found for joinPoint");
            return null;
        }

        // 1. 파라미터 이름 배열 조회
        String[] parameterNames = discoverer.getParameterNames(method);
        if (parameterNames == null || parameterNames.length == 0) {
            log.warn("Parameter names not found. Check -parameters compiler option.");
            return null;
        }

        // 2. 파라미터 값 배열 조회
        Object[] args = joinPoint.getArgs();

        // 3. 이름과 값 매핑하여 ID 탐색
        for (int i = 0; i < parameterNames.length; i++) {
            if (idParamName.equals(parameterNames[i])) {
                return convertToLong(args[i], parameterNames[i]);
            }
        }

        log.debug("ID parameter '{}' not found in method {}", idParamName, method.getName());
        return null;
    }

    /**
     * ID 값을 Long 으로 변환 (다양한 타입 지원)
     */
    private Long convertToLong(Object value, String paramName) {
        if (value == null) {
            log.debug("Parameter '{}' is null", paramName);
            return null;
        }

        try {
            // Long 타입이면 바로 반환
            if (value instanceof Long) {
                return (Long) value;
            }

            // Integer 타입이면 변환
            if (value instanceof Integer) {
                return ((Integer) value).longValue();
            }

            // String 타입이면 변환
            if (value instanceof String) {
                return Long.valueOf((String) value);
            }

            // UUID 타입이면 hashCode 사용 (또는 toString)
            if (value instanceof UUID) {
                return ((UUID) value).getMostSignificantBits();
            }

            // 그 외 타입은 toString 후 변환 시도
            return Long.valueOf(value.toString());

        } catch (Exception e) {
            log.warn("Failed to convert parameter '{}' value '{}' to Long: {}",
                    paramName, value, e.getMessage());
            return null;
        }
    }

    /**
     * [유틸] 모든 파라미터를 Map 으로 변환 (디버깅용)
     */
    public Map<String, Object> getAllArguments(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        if (method == null) return new HashMap<>();

        String[] parameterNames = discoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        Map<String, Object> paramMap = new LinkedHashMap<>();

        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
        }
        return paramMap;
    }

    /**
     * [유틸] 특정 타입의 파라미터 값 추출 (예: DTO 찾기)
     */
    public <T> T getArgumentByType(ProceedingJoinPoint joinPoint, Class<T> targetType) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && targetType.isInstance(arg)) {
                return targetType.cast(arg);
            }
        }
        return null;
    }
}