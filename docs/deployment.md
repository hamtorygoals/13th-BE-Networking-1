# 배포 가이드

## 1. 아키텍처 다이어그램

```
[사용자]
   |
   | HTTPS (443)
   ↓
[<EC2_PUBLIC_IP>.nip.io]
   |
   ↓
[Nginx - EC2]
   | HTTP (8080, 내부)
   ↓
[Spring Boot 컨테이너 - cotato-app]
   | JDBC (3306, Docker 내부 네트워크)
   ↓
[MySQL 컨테이너 - cotato-db]
   |
   ↓
[Docker Volume - mysql-data]  ← 컨테이너 재시작 시 데이터 유지


[feat/networking_2 브랜치 push]
   |
   ↓
[GitHub Actions]
   | 1. Docker 이미지 빌드 (멀티 스테이지)
   ↓
[Docker Hub]
   | 2. SCP로 docker-compose.yml 전송
   | 3. SSH 접속 → .env 생성 → DB 기동 → App 배포
   ↓
[EC2 서버]
```

---

## 2. 배포 URL

| 항목 | URL |
|---|---|
| 서비스 주소 | `https://<EC2_PUBLIC_IP>.nip.io` |
| Swagger UI | `https://<EC2_PUBLIC_IP>.nip.io/swagger-ui/index.html` |

---

## 3. 배포된 Swagger 접속 화면

> 캡처 이미지 첨부
> ![img_1.png](img_1.png)

<!-- ![Swagger 배포 화면](./images/swagger-prod.png) -->

---

## 4. GitHub Actions 성공 화면

> GitHub Actions Workflow 성공 화면 캡처를 아래에 첨부하세요.
> ![img_2.png](img_2.png)

<!-- ![GitHub Actions 성공](./images/github-actions-success.png) -->

---

## 5. Dockerfile 설명

```dockerfile
# Stage 1: Gradle로 JAR 빌드
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 의존성 파일만 먼저 복사 → Docker 레이어 캐시 활용 (소스만 바뀌면 재다운로드 없음)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

COPY src/ src/
RUN ./gradlew bootJar --no-daemon

# Stage 2: JRE만 포함된 경량 이미지로 실행
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**멀티 스테이지 빌드를 선택한 이유:**
- JDK(빌드용)와 JRE(실행용)를 분리해 최종 이미지 크기를 줄임
- 빌드 도구(Gradle 등)가 운영 이미지에 포함되지 않아 공격 표면 감소
- `dependencies` 레이어를 분리해 소스 변경 시 의존성 재다운로드 방지

---

## 6. Nginx 설정

EC2에서 `/etc/nginx/sites-available/cotato`에 아래 내용을 작성합니다.

```nginx
server {
    listen 80;
    server_name <EC2_PUBLIC_IP>.nip.io;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

설정 적용:
```bash
sudo ln -s /etc/nginx/sites-available/cotato /etc/nginx/sites-enabled/
sudo rm /etc/nginx/sites-enabled/default
sudo nginx -t
sudo systemctl reload nginx
```

Certbot으로 HTTPS 적용:
```bash
sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d <EC2_PUBLIC_IP>.nip.io
```

---

## 7. EC2 서버 초기 세팅 순서

Docker 공식 저장소에서 설치합니다. (Ubuntu 기본 저장소는 docker-compose-plugin이 없을 수 있음)

```bash
# Docker 공식 저장소 등록 및 설치
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# sudo 없이 docker 사용 설정
sudo usermod -aG docker ubuntu
newgrp docker
```

이후 `feat/networking_2` 브랜치에 push하면 GitHub Actions가 아래를 자동으로 처리합니다:
- `docker-compose.yml` SCP 전송
- `.env` 파일 생성 (ENV_FILE Secret에서)
- DB 컨테이너 기동 및 healthcheck 대기
- App 컨테이너 최신 이미지로 교체

---

## 8. GitHub Secrets 등록 항목

| Secret 이름 | 설명 |
|---|---|
| `DOCKER_USERNAME` | Docker Hub 아이디 |
| `DOCKER_PASSWORD` | Docker Hub 비밀번호 또는 Access Token |
| `EC2_HOST` | EC2 퍼블릭 IP 주소 |
| `EC2_USER` | EC2 접속 사용자 (`ubuntu`) |
| `EC2_KEY` | `.pem` 파일 내용 전체 (-----BEGIN RSA PRIVATE KEY----- 포함) |
| `ENV_FILE` | `.env` 파일 내용 전체 (DB 비밀번호, Docker 이미지명 등) |

`ENV_FILE` 예시:
```
DB_ROOT_PASSWORD=...
DB_USERNAME=...
DB_PASSWORD=...
DB_NAME=...
DOCKER_IMAGE=도커허브아이디/cotato-backend:latest
```

---

## 9. 트러블슈팅 노트

### 문제 1

```
문제:
컨테이너 내부에서 localhost로 MySQL에 접속 실패

원인:
Docker 네트워크에서 localhost는 같은 컨테이너 자신을 가리킴
MySQL은 별도 컨테이너(db)에 있으므로 서비스 이름으로 접근해야 함

해결:
docker-compose의 app 서비스 환경변수에 DB_HOST=db 설정
application.yml의 datasource URL을 ${DB_HOST:localhost}로 파라미터화
```

### 문제 2

```
문제:
GitHub Actions에서 docker compose up -d 실행 시 수분 이상 멈춤

원인:
docker compose up -d가 MySQL healthcheck를 통과할 때까지 블로킹됨
DB 컨테이너가 재시작되면서 healthcheck 대기 시간이 길어짐

해결:
배포 시 app 컨테이너만 재시작하도록 --force-recreate --no-deps 옵션 사용
DB는 별도로 먼저 기동하고 healthcheck 통과를 확인한 뒤 app 실행
```

### 문제 3

```
문제:
EC2에서 Docker 설치 중 "No space left on device" 오류 발생

원인:
EC2 스토리지를 기본값 8GB로 설정해 Docker 패키지 설치 공간 부족

해결:
EC2 인스턴스 스토리지를 20GB로 설정하여 재생성
```
