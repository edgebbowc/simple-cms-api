# Simple CMS API

Spring Boot 기반의 콘텐츠 관리 REST API 서버입니다.
JWT 인증을 통한 사용자 관리와 Role 기반 접근 제어를 구현했습니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 25 |
| Framework | Spring Boot 4.0 |
| Security | Spring Security 7 + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | H2 (In-Memory) |
| Build | Gradle |
| Monitoring | P6Spy (SQL 로깅) |

---

## 프로젝트 실행 방법

### 1. 사전 요구사항
- JDK 25 이상

### 2. 프로젝트 클론 및 실행

```
git clone https://github.com/edgebbowc/simple-cms-api.git
cd simple-cms-api
./gradlew bootRun
```

### 3. 애플리케이션 확인
- API 서버: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:test`
  - Username: `sa`
  - Password: (공백)

### 4. 테스트 계정

| Role | Username | Password |
|------|----------|----------|
| ADMIN | admin | admin123 |
| USER | user1 | user1234 |
| USER | user2 | user1234 |

---

## 구현 내용

### 인증 (Authentication)
- **JWT 기반 Stateless 인증** 방식 선택
  - Session 방식은 서버 확장 시 세션 불일치 문제가 발생하므로 JWT 채택
- **Access Token** (만료: 30분) + **Refresh Token** (만료: 7일) 이중 토큰 구조
- **Refresh Token Rotation**: 재발급 시 Refresh Token도 함께 교체하여 보안 강화
- Refresh Token은 DB에 저장하여 서버 측 무효화(로그아웃) 가능

### 인가 (Authorization)
- Role 기반 접근 제어 (RBAC): `ADMIN`, `USER`
- **콘텐츠 수정/삭제**: 작성자 본인 또는 ADMIN만 가능
- Spring Security 레벨(Filter)과 서비스 레벨(checkPermission) 이중 권한 검증

### 콘텐츠 관리
- CRUD 기능 구현
- 목록 조회 **페이징 처리** (기본 10개, 최신순 정렬)
- 상세 조회 시 **조회수 자동 증가**

### 예외 처리
- `GlobalExceptionHandler`로 전역 예외 처리
- Spring Security 레벨 예외 커스터마이징
  - 비로그인 요청 → `401 Unauthorized`
- 서비스 레벨 예외
  - 리소스 없음 → `404 Not Found`
  - 권한 없음 → `403 Forbidden`
  - 유효성 검증 실패 → `400 Bad Request`

---

## 추가 구현 기능

| 기능 | 설명 |
|------|------|
| Refresh Token | Access Token 만료 시 재발급 API 구현 |
| Refresh Token Rotation | 재발급 시 Refresh Token도 교체하여 탈취 피해 최소화 |

---

## 사용한 AI 도구 및 참고 자료

- **Perplexity AI**: 프로젝트 구조 설계 및 코드 구현 참고
- [Spring Security 공식 문서](https://docs.spring.io/spring-security/reference/)
- [jjwt 공식 문서](https://github.com/jwtk/jjwt)# Simple CMS API

Spring Boot 기반의 콘텐츠 관리 REST API 서버입니다.
JWT 인증을 통한 사용자 관리와 Role 기반 접근 제어를 구현했습니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 25 |
| Framework | Spring Boot 4.0 |
| Security | Spring Security 7 + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | H2 (In-Memory) |
| Build | Gradle |
| Monitoring | P6Spy (SQL 로깅) |

---

## 프로젝트 실행 방법

### 1. 사전 요구사항
- JDK 25 이상

### 2. 프로젝트 클론 및 실행

\`\`\`bash
git clone https://github.com/{username}/simple-cms-api.git
cd simple-cms-api
./gradlew bootRun
\`\`\`

### 3. 애플리케이션 확인
- API 서버: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:test`
  - Username: `sa`
  - Password: (공백)

### 4. 테스트 계정

| Role | Username | Password |
|------|----------|----------|
| ADMIN | admin | admin123 |
| USER | user1 | user1234 |
| USER | user2 | user1234 |

---

## 구현 내용

### 인증 (Authentication)
- **JWT 기반 Stateless 인증** 방식 선택
  - Session 방식은 서버 확장 시 세션 불일치 문제가 발생하므로 JWT 채택
- **Access Token** (만료: 30분) + **Refresh Token** (만료: 7일) 이중 토큰 구조
- **Refresh Token Rotation**: 재발급 시 Refresh Token도 함께 교체하여 보안 강화
- Refresh Token은 DB에 저장하여 서버 측 무효화(로그아웃) 가능

### 인가 (Authorization)
- Role 기반 접근 제어 (RBAC): `ADMIN`, `USER`
- **콘텐츠 수정/삭제**: 작성자 본인 또는 ADMIN만 가능
- Spring Security 레벨(Filter)과 서비스 레벨(checkPermission) 이중 권한 검증

### 콘텐츠 관리
- CRUD 기능 구현
- 목록 조회 **페이징 처리** (기본 10개, 최신순 정렬)
- 상세 조회 시 **조회수 자동 증가**

### 예외 처리
- `GlobalExceptionHandler`로 전역 예외 처리
- Spring Security 레벨 예외 커스터마이징
  - 비로그인 요청 → `401 Unauthorized`
  - Role 부족 → `403 Forbidden`
- 서비스 레벨 예외
  - 리소스 없음 → `404 Not Found`
  - 권한 없음 → `403 Forbidden`
  - 유효성 검증 실패 → `400 Bad Request`

---

## 추가 구현 기능

| 기능 | 설명 |
|------|------|
| Refresh Token | Access Token 만료 시 재발급 API 구현 |
| Refresh Token Rotation | 재발급 시 Refresh Token도 교체하여 탈취 피해 최소화 |
| P6Spy | 실행 SQL 및 파라미터 바인딩 값 로깅 |
| Virtual Thread | `spring.threads.virtual.enabled: on` 설정으로 Java 25 가상 스레드 활성화 |
| 조회수 | 콘텐츠 상세 조회 시 view_count 자동 증가 |

---

## 사용한 AI 도구 및 참고 자료

- **Perplexity AI**: 프로젝트 구조 설계 및 코드 구현 참고
- [Spring Security 공식 문서](https://docs.spring.io/spring-security/reference/)


---

## API 명세

### 공통

- Base URL: `http://localhost:8080`
- 인증이 필요한 API는 Header에 포함
  ```
  Authorization: Bearer {accessToken}
  ```

---

### 인증 API

#### 로그인
```
POST /api/auth/login
```
Request Body:
```json
{
  "username": "user1",
  "password": "user1234"
}
```
Response:
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "role": "USER"
}
```

#### Access Token 재발급
```
POST /api/auth/reissue
Header: Refresh-Token: {refreshToken}
```
Response:
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci..."
}
```

#### 로그아웃
```
POST /api/auth/logout
Header: Authorization: Bearer {accessToken}
```
Response: `204 No Content`

---

### 콘텐츠 API

#### 목록 조회 (페이징)
```
GET /api/contents?page=0&size=10
```
Response:
```json
{
  "content": [
    {
      "id": 1,
      "title": "제목",
      "description": "내용",
      "viewCount": 5,
      "createdBy": "user1",
      "createdDate": "2026-03-08T18:00:00"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
```

#### 상세 조회
```
GET /api/contents/{id}
```

Response:
```json
{
    "id": 1,
    "title": "공지사항",
    "description": "환영합니다!",
    "viewCount": 1,
    "createdBy": "admin",
    "createdDate": "2026-03-08T21:36:28.405934",
    "lastModifiedBy": null,
    "lastModifiedDate": null
}
```

#### 콘텐츠 생성 🔒
```
POST /api/contents
Header: Authorization: Bearer {accessToken}
```
Request Body:
```json
{
  "title": "제목",
  "description": "내용"
}
```
Response:
```json
{
    "id": 3,
    "title": "제목",
    "description": "내용",
    "viewCount": 0,
    "createdBy": "user1",
    "createdDate": "2026-03-08T21:55:42.2742462",
    "lastModifiedBy": "user1",
    "lastModifiedDate": "2026-03-08T21:55:42.2742462"
}
```
#### 콘텐츠 수정 🔒 (본인/ADMIN)
```
PUT /api/contents/{id}
Header: Authorization: Bearer {accessToken}
```
Request Body:
```json
{
  "title": "수정된 제목",
  "description": "수정된 내용"
}
```

Response:
```json
{
    "id": 2,
    "title": "수정된 제목",
    "description": "수정된 내용",
    "viewCount": 0,
    "createdBy": "user1",
    "createdDate": "2026-03-08T21:51:09.66255",
    "lastModifiedBy": "user1",
    "lastModifiedDate": null
}
```

#### 콘텐츠 삭제 🔒 (본인/ADMIN)
```
DELETE /api/contents/{id}
Header: Authorization: Bearer {accessToken}
```
Response: `204 No Content`

---

### 에러 응답 형식

```json
{
  "status": 403,
  "message": "수정/삭제 권한이 없습니다."
}
```

| 코드 | 설명 |
|------|------|
| 400 | 요청값 유효성 오류 |
| 401 | 로그인 필요 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 500 | 서버 오류 |






