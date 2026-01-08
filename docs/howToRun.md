# 로컬 실행 가이드 (Doppler 기반)

## 전제 조건
- **!중요** 반드시 `masil-be` 디렉터리에서 실행
- Doppler CLI 로그인 및 프로젝트 설정 완료
- Docker 실행 중
- PostgreSQL 컨테이너는 docker compose로 관리
- docker compose up 명령은 상황에 맞게 하나만 실행

---

## 로컬 인프라 실행 (PostgreSQL)

```
# 로그 확인용 실행 (추천)
doppler run -- docker compose up --build

# 백그라운드 실행
doppler run -- docker compose up -d --build
```

---

## 컨테이너 제어

```
# 컨테이너만 중지 (데이터 유지)
doppler run -- docker compose stop

# 컨테이너 + 네트워크 종료 (데이터 유지, 추천)
doppler run -- docker compose down

# 컨테이너 + 네트워크 + 볼륨 삭제 (DB 초기화, 주의)
doppler run -- docker compose down -v
```

---

## 애플리케이션 실행 (Spring Boot)

```
# Windows
doppler run -- .\gradlew bootRun

# macOS / Linux
doppler run -- ./gradlew bootRun
```
