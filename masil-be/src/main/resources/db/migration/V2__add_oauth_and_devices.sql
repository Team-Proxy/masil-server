-- ======================================================
-- V2__add_oauth_and_devices.sql
-- OAuth 계정 연동 및 사용자 디바이스 관리 테이블
-- ======================================================

/*
    설계 목적
    - OAuth(NAVER, KAKAO, GOOGLE) 계정과 내부 user 계정 연결 (GOOGLE은 후순위)
    - 하나의 사용자 계정에 다중 OAuth 연동 허용
    - 사용자별 다중 디바이스 접속 관리
    - "모든 디바이스 로그아웃" 구현 기반 제공
*/

-- ======================================================
-- OAuth 계정 연동 테이블
-- ======================================================
CREATE TABLE "user_oauth_accounts" (
       "user_oauth_account_id" BIGSERIAL PRIMARY KEY,
       "user_id" BIGINT NOT NULL,
       "provider" VARCHAR(20) NOT NULL,
       "provider_user_id" VARCHAR(100) NOT NULL,
       "linked_via" VARCHAR(30) NOT NULL,
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

-- TABLE COMMENT
COMMENT ON TABLE "user_oauth_accounts" IS 'OAuth 제공자(NAVER, KAKAO, GOOGLE) 계정과 내부 사용자 계정(users)을 연결하는 테이블';

-- COLUMN COMMENT
COMMENT ON COLUMN "user_oauth_accounts"."user_oauth_account_id" IS 'OAuth 계정 연동 테이블의 기본 키';
COMMENT ON COLUMN "user_oauth_accounts"."user_id" IS '내부 사용자 식별자 (users.user_id 참조)';
COMMENT ON COLUMN "user_oauth_accounts"."provider" IS 'OAuth 제공자 식별자 (NAVER, KAKAO, GOOGLE)';
COMMENT ON COLUMN "user_oauth_accounts"."provider_user_id" IS 'OAuth 제공자 측 사용자 고유 식별자';
COMMENT ON COLUMN "user_oauth_accounts"."linked_via" IS 'OAuth 계정 연동 경로 (OAUTH_LOGIN, ACCOUNT_MERGE, ADMIN)';
COMMENT ON COLUMN "user_oauth_accounts"."created_at" IS 'OAuth 계정이 내부 사용자 계정과 연결된 시각';

-- INDEX
CREATE UNIQUE INDEX "uk_user_oauth_provider_user" ON "user_oauth_accounts" ("provider", "provider_user_id");
COMMENT ON INDEX "uk_user_oauth_provider_user" IS 'OAuth 제공자 + 제공자 사용자 ID 조합의 전역 유니크 보장';

CREATE INDEX "idx_user_oauth_accounts_user" ON "user_oauth_accounts" ("user_id");
COMMENT ON INDEX "idx_user_oauth_accounts_user" IS '사용자 기준 OAuth 계정 조회 최적화';

CREATE INDEX "idx_user_oauth_accounts_user_provider" ON "user_oauth_accounts" ("user_id", "provider");
COMMENT ON INDEX "idx_user_oauth_accounts_user_provider" IS '사용자별 특정 OAuth 제공자 연동 여부 확인용 인덱스';


-- ======================================================
-- 사용자 디바이스 테이블
-- ======================================================
CREATE TABLE "user_devices" (
        "user_device_id" BIGSERIAL PRIMARY KEY,
        "user_id" BIGINT NOT NULL,
        "device_id" UUID NOT NULL,
        "device_type" VARCHAR(30),
        "last_login_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
        "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);

-- TABLE COMMENT
COMMENT ON TABLE "user_devices" IS '사용자별 접속 디바이스 정보를 관리하는 테이블 (다중 디바이스 로그인 및 전체 로그아웃 지원)';

-- COLUMN COMMENT
COMMENT ON COLUMN "user_devices"."user_device_id" IS '사용자 디바이스 테이블의 기본 키';
COMMENT ON COLUMN "user_devices"."user_id" IS '내부 사용자 식별자 (users.user_id 참조)';
COMMENT ON COLUMN "user_devices"."device_id" IS '클라이언트에서 생성한 디바이스 고유 식별자(UUID)';
COMMENT ON COLUMN "user_devices"."device_type" IS '디바이스 유형 (WEB, ANDROID, IOS)';
COMMENT ON COLUMN "user_devices"."last_login_at" IS '해당 디바이스의 마지막 로그인 시각';
COMMENT ON COLUMN "user_devices"."created_at" IS '디바이스가 최초 등록된 시각';

-- INDEX
CREATE UNIQUE INDEX "uk_user_devices_user_device" ON "user_devices" ("user_id", "device_id");
COMMENT ON INDEX "uk_user_devices_user_device" IS '하나의 사용자 계정에서 동일 디바이스 중복 등록 방지';

CREATE INDEX "idx_user_devices_user" ON "user_devices" ("user_id");
COMMENT ON INDEX "idx_user_devices_user" IS '사용자 기준 활성 디바이스 목록 조회용 인덱스';

CREATE INDEX "idx_user_devices_device" ON "user_devices" ("device_id");
COMMENT ON INDEX "idx_user_devices_device" IS '디바이스 단건 조회 및 로그아웃 처리용 인덱스';
