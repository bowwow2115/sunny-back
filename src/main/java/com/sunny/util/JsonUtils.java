package com.sunny.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    /**
     * Object 를 Map 으로 변환 (연관관계 제외, 순환 참조 방지)
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) return null;

        try {
            Map<String, Object> map = new LinkedHashMap<>();
            Class<?> clazz = getEntityClass(obj);

            for (Field field : getAllFields(clazz)) {
                field.setAccessible(true);

                // 제외할 필드 필터링
                if (shouldExcludeField(field)) {
                    continue;
                }

                Object value = field.get(obj);
                if (value != null && !isJPAProxy(value)) {
                    map.put(field.getName(), convertValue(value));
                }
            }

            return map;
        } catch (Exception e) {
            log.warn("Map 변환 실패: {}", e.getMessage());
            return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
        }
    }

    /**
     * Object 를 Map 으로 변환 (지정 필드 제외)
     */
    public static Map<String, Object> toMapExcludeFields(Object obj, String... excludeFields) {
        Map<String, Object> map = toMap(obj);
        if (map == null || excludeFields == null) return map;

        for (String field : excludeFields) {
            map.remove(field);
        }
        return map;
    }

    /**
     * ⭐ 두 객체의 차이점만 Map 으로 추출 (Diff)
     */
    public static Map<String, Object> toDiffMap(Object oldObj, Object newObj) {
        if (oldObj == null && newObj == null) {
            return null;
        }

        try {
            Map<String, Object> diffMap = new LinkedHashMap<>();

            // 1. 신규 생성 (oldObj 가 null)
            if (oldObj == null) {
                diffMap.put("changeType", "CREATE");
                diffMap.put("newValue", toMap(newObj));
                return diffMap;
            }

            // 2. 삭제 (newObj 가 null)
            if (newObj == null) {
                diffMap.put("changeType", "DELETE");
                diffMap.put("oldValue", toMap(oldObj));
                return diffMap;
            }

            // 3. 수정 (둘 다 존재)
            Map<String, Object> oldMap = toMap(oldObj);
            Map<String, Object> newMap = toMap(newObj);

            Map<String, Object> changes = new LinkedHashMap<>();

            // 모든 필드 순회 (old + new 합집합)
            Set<String> allKeys = new HashSet<>();
            allKeys.addAll(oldMap.keySet());
            allKeys.addAll(newMap.keySet());

            for (String key : allKeys) {
                Object oldValue = oldMap.get(key);
                Object newValue = newMap.get(key);

                // 값이 변경된 경우만 기록
                if (!Objects.equals(oldValue, newValue)) {
                    Map<String, Object> change = new LinkedHashMap<>();
                    change.put("from", oldValue);
                    change.put("to", newValue);
                    changes.put(key, change);
                }
            }

            // 변경된 필드가 없으면 null 반환
            if (changes.isEmpty()) {
                return null;
            }

            diffMap.put("changeType", "UPDATE");
            diffMap.put("changes", changes);
            return diffMap;

        } catch (Exception e) {
            log.warn("Diff Map 생성 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     *   Diff 를 사람이 읽기 쉬운 문자열로 변환
     */
    public static String toDiffString(Object oldObj, Object newObj) {
        Map<String, Object> diffMap = toDiffMap(oldObj, newObj);
        if (diffMap == null) return null;

        StringBuilder sb = new StringBuilder();
        String changeType = (String) diffMap.get("changeType");
        sb.append("[").append(changeType).append("] ");

        if ("CREATE".equals(changeType)) {
            sb.append("신규 생성");
        } else if ("DELETE".equals(changeType)) {
            sb.append("삭제");
        } else if ("UPDATE".equals(changeType)) {
            Map<String, Object> changes = (Map<String, Object>) diffMap.get("changes");
            List<String> changeList = new ArrayList<>();

            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                String field = entry.getKey();
                Map<String, Object> change = (Map<String, Object>) entry.getValue();
                Object from = change.get("from");
                Object to = change.get("to");
                changeList.add(field + ": " + from + " → " + to);
            }

            sb.append(String.join(", ", changeList));
        }

        return sb.toString();
    }

    // ==================== 유틸리티 메서드 ====================

    /**
     * 제외할 필드 판단
     */
    private static boolean shouldExcludeField(Field field) {
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();

        // 1. 컬렉션 타입 제외 (순환 참조 방지)
        if (Collection.class.isAssignableFrom(fieldType)) {
            return true;
        }

        // 2. JPA 연관관계 어노테이션 확인
        if (field.isAnnotationPresent(OneToMany.class) ||
                field.isAnnotationPresent(ManyToMany.class) ||
                field.isAnnotationPresent(OneToOne.class)) {
            return true;
        }

        // 3. Hibernate 프록시 제외
        if (fieldType.getName().contains("Hibernate")) {
            return true;
        }

        // 4. 일반적 제외 필드
        if (fieldName.equals("hibernateLazyInitializer") ||
                fieldName.equals("handler") ||
                fieldName.equals("version") ||
                fieldName.equals("createdAt") ||
                fieldName.equals("updatedAt") ||
                fieldName.equals("createdBy") ||
                fieldName.equals("modifiedBy")) {
            return true;
        }

        return false;
    }

    /**
     * 모든 필드 추출 (상속 포함)
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * JPA 프록시 확인
     */
    private static boolean isJPAProxy(Object value) {
        if (value == null) return false;
        String className = value.getClass().getName();
        return className.contains("$HibernateProxy") ||
                className.contains("PersistentBag") ||
                className.contains("PersistentSet") ||
                className.contains("PersistentList");
    }

    /**
     * 값 변환 (단순 타입만)
     */
    private static Object convertValue(Object value) {
        if (value == null) return null;

        // 단순 타입은 그대로 반환
        if (value instanceof String || value instanceof Number ||
                value instanceof Boolean || value instanceof LocalDateTime ||
                value instanceof LocalDate || value instanceof LocalTime) {
            return value;
        }

        // Enum 은 문자열로
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }

        // 엔티티라면 ID 만 추출
        if (value.getClass().isAnnotationPresent(Entity.class)) {
            return extractId(value);
        }

        // 그 외는 문자열로 변환
        return value.toString();
    }

    /**
     * 엔티티에서 ID 추출
     */
    private static Long extractId(Object obj) {
        try {
            return Arrays.stream(obj.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(f -> {
                        f.setAccessible(true);
                        try {
                            return f.get(obj);
                        } catch (IllegalAccessException e) {
                            return null;
                        }
                    })
                    .map(id -> {
                        if (id instanceof Long) return (Long) id;
                        if (id instanceof Integer) return ((Integer) id).longValue();
                        return null;
                    })
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JPA 프록시 클래스 실제 클래스로 변환
     */
    private static Class<?> getEntityClass(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.getName().contains("$HibernateProxy") ||
                clazz.getName().contains("$EnhancerBySpringCGLIB")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    /**
     * Object 를 JSON 문자열로 변환 (디버깅용)
     */
    public static String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("JSON 변환 실패: {}", e.getMessage());
            return "{}";
        }
    }

    /**
     * Map 을 JSON 문자열로 변환 (DB 저장용)
     */
    public static String mapToJson(Map<String, Object> map) {
        if (map == null) return null;
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.warn("JSON 변환 실패: {}", e.getMessage());
            return "{}";
        }
    }
}