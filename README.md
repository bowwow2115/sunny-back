# Sunny Backend

Spring Boot 기반 백엔드 서버입니다. RESTful API, JWT 인증, PostgreSQL 데이터베이스를 활용한 확장 가능한 애플리케이션 구조를 제공합니다.
해당 어플리케이션은 어린이집에서 원아을 관리하기 위한 목적으로 만들어진 어플리케이션으로서 원아관리, 원아 등하원 차량관리 기능 등을 제공하고 있습니다.

## 📋 프로젝트 정보

| 항목                 | 내용          |
| -------------------- | ------------- |
| **프로젝트명**       | Sunny Backend |
| **버전**             | 1.0           |
| **Java 버전**        | 17            |
| **Spring Boot 버전** | 3.4.2         |
| **빌드 도구**        | Gradle        |
| **데이터베이스**     | PostgreSQL    |

## 🛠 기술 스택

### Backend Framework

- **Spring Boot 3.4.2** - REST API 서버 구성
- **Spring Security** - 인증/인가 관리
- **Spring Data JPA** - ORM 및 데이터 접근

### Database

- **PostgreSQL** - 주 데이터베이스
- **MyBatis** - SQL 매핑
- **QueryDSL** - 타입안전 쿼리 작성
- **Hibernate** - JPA 구현체

### Authentication

- **JWT (JSON Web Token)** - 토큰 기반 인증
- **Spring Security** - 권한 관리

### Utilities

- **Lombok** - 보일러플레이트 코드 자동화
- **Apache POI** - Excel 파일 처리
- **Logback** - 로깅 관리

## 📁 프로젝트 구조

```
src/main/
├── java/com/sunny/
│   ├── SunnyApplication.java       # 애플리케이션 진입점
│   ├── BootstrapData.java          # 초기 데이터 로드
│   ├── config/                      # 설정 클래스들
│   ├── controller/                  # REST API 컨트롤러
│   ├── service/                     # 비즈니스 로직
│   ├── repository/                  # 데이터 접근 계층
│   ├── model/                       # 엔티티 및 DTO
│   ├── code/                        # 상수/열거형
│   └── util/                        # 유틸리티 클래스
└── resources/
    ├── application.yml              # 애플리케이션 설정
    ├── logback-spring.xml           # 로그 설정
    └── static/                      # 정적 리소스 (HTML, CSS, JS)
```

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Gradle 8.0 이상
- PostgreSQL 12 이상
- Git

### 환경 설정

#### 1. 저장소 클론

```bash
git clone <repository-url>
cd sunny-back
```

#### 2. PostgreSQL 데이터베이스 설정

```bash
# PostgreSQL 접속
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE postgres;

# 사용자 생성 (이미 존재하는 경우 생략)
CREATE USER sunny WITH PASSWORD 'sunny';

# 권한 부여
GRANT ALL PRIVILEGES ON DATABASE postgres TO sunny;
```

#### 3. 애플리케이션 설정 수정

[src/main/resources/application.yml](src/main/resources/application.yml)에서 다음 항목을 환경에 맞게 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<your_postgresql_url>
    username: <your_username>
    password: <your_password>

server:
  port: 8080
  servlet:
    context-path: /sunny

jwt:
  jwtAccessHours: 120
  jwtRefreshDay: 7
  secret: <your-jwt-secret-key>

jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 1000
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        default_schema: sunny
    show-sql: true
    hibernate:
      ddl-auto: validate
```

### 빌드 및 실행

#### Gradle Wrapper를 이용한 빌드

```bash
# Windows
gradlew.bat build

# Linux/Mac
./gradlew build
```

#### 애플리케이션 실행

```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

또는 IDE에서 `SunnyApplication` 클래스를 직접 실행할 수 있습니다.

#### 빌드 결과물 실행

```bash
java -jar build/libs/sunny-1.0.jar
```

### 접속 정보

- **API Base URL**: http://localhost:8080/sunny
- **Context Path**: /sunny

## 📖 API 문서

### 인증 관련 API

JWT 토큰을 활용한 인증 기반 API입니다.

#### 로그인

```http
POST /sunny/api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

#### 토큰 갱신

```http
POST /sunny/api/auth/refresh
Authorization: Bearer <refresh_token>
```

### API 응답 형식

```json
{
  "code": "SUCCESS",
  "message": "요청이 성공했습니다.",
  "data": {}
}
```

## 🧪 테스트

### 테스트 실행

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests com.sunny.repository.ChildRepositoryTest
```

### 테스트 보고서

테스트 후 생성된 보고서 위치:

- `build/reports/tests/test/index.html`

## 🔐 보안

### JWT 설정

- **Access Token 유효시간**: 120시간 (설정 가능)
- **Refresh Token 유효기간**: 7일 (설정 가능)
- **Secret Key**: [application.yml](src/main/resources/application.yml)에서 설정

**주의**: 배포 시에는 반드시 Secret Key를 강력한 값으로 변경하세요.

### Spring Security

- 모든 엔드포인트는 인증 기반 접근 제어
- CORS 설정 제공 (`domain: "*"`)

## 📝 주요 설정 항목

| 설정                            | 기본값         | 설명                         |
| ------------------------------- | -------------- | ---------------------------- |
| `server.port`                   | 8080           | 서버 포트                    |
| `server.servlet.context-path`   | /sunny         | API 경로 접두사              |
| `spring.datasource.url`         | localhost:5432 | PostgreSQL 연결 URL          |
| `jwt.jwtAccessHours`            | 120            | Access Token 유효시간 (시간) |
| `jwt.jwtRefreshDay`             | 7              | Refresh Token 유효기간 (일)  |
| `spring.jpa.hibernate.ddl-auto` | validate       | JPA DDL 자동 생성 정책       |

## 📊 데이터베이스 스키마

### 주요 테이블

데이터베이스 스키마는 JPA 엔티티로 관리됩니다.

```bash
# 엔티티 위치
src/main/java/com/sunny/model/
```

## 🐛 문제 해결

### QueryDSL 컴파일 오류

```bash
# 캐시 초기화 후 다시 빌드
./gradlew clean build
```

**마지막 업데이트**: 2026년 2월 19일
