package com.sunny.config.aop;

import com.sunny.model.BusinessHistory;
import com.sunny.service.HistoryService;
import com.sunny.util.JsonUtils;
import com.sunny.util.RequestUtil;
import com.sunny.util.UserContextUtil;
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
    private static final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(trackHistory)")
    public Object logHistory(ProceedingJoinPoint joinPoint, TrackHistory trackHistory) throws Throwable {
        // [Before]
        // 1. targetId가 필요 없는 경우
        if(trackHistory.noTargetId()) {
            Object result = joinPoint.proceed();
            BusinessHistory history = createHistory(null, trackHistory, null);
            historyService.asyncCreate(history);
            return result;
        }

        // 2. 파라미터에서 ID 시도 (수정/삭제용)
        List<Long> targetIdList = extractIdFromArgs(joinPoint, trackHistory.idParamName());

        // 2. 비즈니스 로직 실행
        Object result = joinPoint.proceed();

        // 3. ID 재확인 (생성 케이스용)
        if (targetIdList == null && trackHistory.idFromReturn() && result != null) {
            targetIdList = extractIdFromObject(result, trackHistory.targetType());
        }

        // 5. 새 값 추출
        String httpMethod = requestUtil.getHttpMethod();
        Object newValue = null;

        if (httpMethod != null && (httpMethod.equalsIgnoreCase("POST") || httpMethod.equalsIgnoreCase("PUT"))) {
            newValue = result;
        }

        if(targetIdList != null) {
            // 4. 이력 객체 생성 및 저장
            for (Long id : targetIdList) {
                BusinessHistory history = createHistory(id, trackHistory, newValue);
                historyService.asyncCreate(history);
            }
        }

        return result;
    }

    private BusinessHistory createHistory(Long targetId, TrackHistory trackHistory, Object newValue) {
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
                .newValue(JsonUtils.toMap(newValue))
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

    private List<Long> extractIdFromObject(Object obj, Class clazz) {
        List<Long> targetIdList = new ArrayList<>();
        if (obj == null) return null;

        try {
            // 1. 필드에서 @Id 탐색 (상속 포함)
            Field idField = findIdField(clazz);
            if (idField != null) {
                //dto 타입에서 값 추출
                String idFieldName = idField.getName();
                Field dtoIdField = obj.getClass().getDeclaredField(idFieldName);
                dtoIdField.setAccessible(true);
                Long idValue = getIdValue(dtoIdField.get(obj));
                targetIdList.add(idValue);
                return targetIdList;
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

    public List<Long> extractIdFromArgs(ProceedingJoinPoint joinPoint, String idParamName) {
        Method method = getMethod(joinPoint);
        if (method == null) {
            log.warn("Method not found for joinPoint: {}", joinPoint.getSignature());
            return null;
        }

        // 1. 파라미터 메타데이터 조회 (컴파일러 -parameters 옵션 필요)
        String[] parameterNames = discoverer.getParameterNames(method);
        if (parameterNames == null || parameterNames.length == 0) {
            log.warn("Parameter names not found. Check compiler option: -parameters");
            return null;
        }

        Object[] args = joinPoint.getArgs();

        // 2. 파라미터 이름으로 타겟 인자 찾기
        for (int i = 0; i < parameterNames.length; i++) {
            if (idParamName.equals(parameterNames[i])) {
                return extractIdFromValue(args[i], idParamName);
            }
        }

        log.debug("ID parameter '{}' not found in method {}", idParamName, method.getName());
        return null;
    }

    /**
     * 주어진 값에서 재귀적으로 ID (Long) 를 추출하는 유틸리티 메서드
     * - 단순 타입: 바로 변환
     * - 컬렉션/배열: 첫 번째 요소의 ID 추출
     * - 객체: "id" 또는 "Id" 필드/게터 탐색
     */
    private List<Long> extractIdFromValue(Object value, String paramName) {
        List<Long> idList = new ArrayList<>();
        if (value == null) {
            log.debug("Argument '{}' is null", paramName);
            return null;
        }

        // Case 1: 이미 추출 가능한 단순 타입인 경우
        if (value instanceof Number) {
            long id = ((Number) value).longValue();
            idList.add(id);
            return idList;
        }
        if (value instanceof String) {
            try {
                long id = Long.parseLong((String) value);
                idList.add(id);
                return idList;
            } catch (NumberFormatException e) {
                log.warn("String ID '{}' cannot be parsed to Long", value);
                return null;
            }
        }

        // Case 2: 컬렉션인 경우 -> id 리스트로 반환
        if (value instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) value;
            if (collection.isEmpty()) {
                log.debug("Collection argument '{}' is empty", paramName);
                return null;
            }
            for(Object obj : collection) {
                List<Long> longs = extractIdFromValue(obj, paramName);
                if (longs != null && longs.size() != 0) {
                    idList.addAll(longs);
                }
            }
            return idList;
        }

        // Case 3: 배열인 경우 -> 아이디 배열로 반환
        if (value.getClass().isArray()) {
            Long[] array = (Long[]) value;
            if (array.length == 0) {
                log.debug("Array argument '{}' is empty", paramName);
                return null;
            }
            idList = Arrays.asList(array);
            return idList;
        }

        // Case 4: 커스텀 객체 (DTO, Entity) 인 경우 -> 리플렉션으로 id 필드 탐색
        return extractIdFromObject(value, paramName);
    }

    /**
     * 객체 내부에서 'id' 라는 이름을 가진 필드 또는 게터 메서드를 찾아 값을 추출
     */
    private List<Long> extractIdFromObject(Object obj, String paramName) {
        if (obj == null) return null;
        List<Long> idList = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        // 1. Getter 메서드 우선 탐색 (Java Bean convention: getId(), isId())
        try {
            Method getIdMethod = clazz.getMethod("getId");
            if (getIdMethod.getReturnType().equals(Long.class) || getIdMethod.getReturnType().equals(long.class)) {
                Long id = (Long) getIdMethod.invoke(obj);
                idList.add(id);
            }
            // getId() 가 다른 객체를 반환하면 (예: UserId VO) 재귀 탐색
            if (!getIdMethod.getReturnType().isPrimitive() && !getIdMethod.getReturnType().equals(String.class)) {
                Object nestedId = getIdMethod.invoke(obj);
                return extractIdFromValue(nestedId, paramName + ".id");
            }
        } catch (NoSuchMethodException e) {
            // getId() 가 없으면 필드 탐색으로 진행
        } catch (Exception e) {
            log.warn("Failed to invoke getId() on object of type {}", clazz.getName(), e);
        }

        // 2. 필드 직접 탐색 (private 필드 접근 포함)
        try {
            Field idField = findFieldRecursive(clazz, "id");
            if (idField != null) {
                idField.setAccessible(true);
                Object fieldValue = idField.get(obj);
                return extractIdFromValue(fieldValue, paramName + ".id"); // 재귀 호출로 중첩 처리
            }
        } catch (Exception e) {
            log.warn("Failed to access 'id' field on object of type {}", clazz.getName(), e);
        }

        log.debug("Could not extract ID from object of type: {}", clazz.getName());
        return null;
    }

    /**
     * 상위 클래스까지 포함하여 필드를 탐색하는 헬퍼 메서드
     */
    private Field findFieldRecursive(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
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