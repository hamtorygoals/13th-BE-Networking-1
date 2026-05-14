# 코테이토 13기 BE 네트워킹 과제 - 지원자 관리 시스템

## ERD 설계

### 테이블 정의

#### `applicant` (지원자)

| 컬럼명 | 타입 | 제약 | 설명 |
|---|---|---|---|
| applicant_id | BIGINT | PK, AUTO_INCREMENT | 지원자 식별자 |
| name | VARCHAR(30) | NOT NULL | 현재 이름 (수정 가능) |
| age | INT | NOT NULL | 현재 나이 (수정 가능) |
| phone_number | VARCHAR(11) | NOT NULL, UNIQUE | 동일 지원자 식별자 역할, 010 시작 11자리 |
| created_at | DATETIME | NOT NULL | 생성 시각 |
| updated_at | DATETIME | NOT NULL | 수정 시각 |

#### `application` (지원 서류)

| 컬럼명 | 타입 | 제약 | 설명 |
|---|---|---|---|
| application_id | BIGINT | PK, AUTO_INCREMENT | 서류 식별자 |
| applicant_id | BIGINT | FK → applicant | 지원자 참조 |
| name | VARCHAR(30) | NOT NULL | 제출 당시 이름 스냅샷 |
| period | INT | NOT NULL | 지원 기수 (1 이상) |
| age | INT | NOT NULL | 제출 당시 나이 (22~30) |
| part | VARCHAR(20) | NOT NULL | 지원 파트 (PLANNING/DESIGN/FRONTEND/BACKEND) |
| ability | INT | NOT NULL | 실력 점수 (0~10) |
| passion | INT | NOT NULL | 열정 점수 (0~10) |
| phone_number | VARCHAR(11) | NOT NULL | 제출 당시 전화번호 스냅샷 |
| application_time | DATETIME | NOT NULL | 서류 제출 시간 |
| created_at | DATETIME | NOT NULL | 레코드 생성 시각 |
| updated_at | DATETIME | NOT NULL | 레코드 수정 시각 |

#### `staff` (운영진)

| 컬럼명 | 타입 | 제약 | 설명 |
|---|---|---|---|
| staff_id | BIGINT | PK, AUTO_INCREMENT | 운영진 식별자 |
| name | VARCHAR(30) | NOT NULL | 이름 (수정 가능) |
| age | INT | NOT NULL | 나이 (수정 가능) |
| phone_number | VARCHAR(11) | NOT NULL | 연락처 (수정 가능) |
| role | VARCHAR(30) | NOT NULL | 역할 enum (PART_LEADER/PLANNING_LEADER/PR_LEADER/VICE_PRESIDENT/PRESIDENT/EDUCATION_LEADER) |
| created_at | DATETIME | NOT NULL | 생성 시각 |
| updated_at | DATETIME | NOT NULL | 수정 시각 |

#### `application_like` (좋아요)

| 컬럼명 | 타입 | 제약 | 설명 |
|---|---|---|---|
| like_id | BIGINT | PK, AUTO_INCREMENT | 좋아요 식별자 |
| application_id | BIGINT | FK → application | 서류 참조 |
| staff_id | BIGINT | FK → staff | 운영진 참조 |
| created_at | DATETIME | NOT NULL | 생성 시각 |
| updated_at | DATETIME | NOT NULL | 수정 시각 |
| - | - | UNIQUE(application_id, staff_id) | 중복 좋아요 방지 |

---

## API 명세 (Swagger)

서버 실행 후 아래 주소에서 Swagger UI를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui.html
```

> Swagger UI 캡처 이미지를 아래에 첨부해 주세요.

<!-- ![Swagger](이미지_경로.png) -->

---

## 실행 방법

1. MySQL 데이터베이스 생성

```sql
CREATE DATABASE cotato_networking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. `src/main/resources/application.yml` 에서 DB 비밀번호 수정

```yaml
spring:
  datasource:
    password: MySQL_비밀번호
```

3. 애플리케이션 실행

```bash
./gradlew bootRun
```

---

## 주요 API 요약

### 서류 API

| Method | URL | 설명 |
|---|---|---|
| POST | `/api/applications` | 서류 등록 |
| GET | `/api/applications/{applicationId}` | 서류 상세 조회 |
| GET | `/api/applications?filterBy=likes&page=1` | 서류 목록 (좋아요 순) |
| GET | `/api/applications?filterBy=gisu&period=13&page=1` | 서류 목록 (기수 필터) |
| GET | `/api/applications?filterBy=gisu%2Blikes&period=13&page=1` | 서류 목록 (기수+좋아요 순) |

### 지원자 API

| Method | URL | 설명 |
|---|---|---|
| GET | `/api/applicants/{applicantId}` | 지원자 조회 |
| PUT | `/api/applicants/{applicantId}` | 지원자 정보 수정 |

### 운영진 API

| Method | URL | 설명 |
|---|---|---|
| POST | `/api/staff` | 운영진 등록 |
| GET | `/api/staff/{staffId}` | 운영진 조회 |
| PUT | `/api/staff/{staffId}` | 운영진 정보 수정 |

### 좋아요 API

| Method | URL | 설명 |
|---|---|---|
| POST | `/api/applications/{applicationId}/likes` | 좋아요 추가 |
| DELETE | `/api/applications/{applicationId}/likes?staffId=1` | 좋아요 취소 |
