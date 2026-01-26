/*
    DDL은 Postgresql 기반으로 작성되었습니다.
*/

/*
    회원 도메인 DDL
    "users",
    "user_roles",
    "roles",
    "role_permissions",
    "permissions"
 */
-- 회원 테이블
CREATE TABLE "users" (
     "user_id" BIGSERIAL PRIMARY KEY,
     "user_name" VARCHAR(20) NOT NULL,
     "nickname" VARCHAR(20) NOT NULL,
     "email" VARCHAR(30) NOT NULL,
     "password" VARCHAR(255) NOT NULL,
     "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
     "created_by" BIGINT NOT NULL,
     "updated_at" TIMESTAMP(6) WITH TIME ZONE,
     "updated_by" BIGINT,
     "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
     "deleted_by" BIGINT
);
CREATE UNIQUE INDEX "uk_users_email" ON "users" ("email");

-- 회원-역할 매핑 테이블
CREATE TABLE "user_roles" (
      "user_role_id" BIGSERIAL PRIMARY KEY,
      "user_id" BIGINT NOT NULL,
      "role_id" BIGINT NOT NULL,
      "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
      "created_by" BIGINT NOT NULL,
      "updated_at" TIMESTAMP(6) WITH TIME ZONE,
      "updated_by" BIGINT,
      "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
      "deleted_by" BIGINT
);
CREATE INDEX "idx_user_roles_user" ON "user_roles" ("user_id");
CREATE INDEX "idx_user_roles_role" ON "user_roles" ("role_id");

-- 역할 테이블
CREATE TABLE "roles" (
     "role_id" BIGSERIAL PRIMARY KEY,
     "role_name" VARCHAR(50) NOT NULL,
     "role_description" VARCHAR(255) NOT NULL,
     "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
     "created_by" BIGINT NOT NULL,
     "updated_at" TIMESTAMP(6) WITH TIME ZONE,
     "updated_by" BIGINT,
     "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
     "deleted_by" BIGINT
);

-- 역할-권한 매핑 테이블
CREATE TABLE "role_permissions" (
    "role_permission_id" BIGSERIAL PRIMARY KEY,
    "role_id" BIGINT NOT NULL,
    "permission_id" BIGINT NOT NULL
);
CREATE INDEX "idx_role_permissions_role" ON "role_permissions" ("role_id");
CREATE INDEX "idx_role_permissions_permission" ON "role_permissions" ("permission_id");

-- 권한 테이블
CREATE TABLE "permissions" (
       "permission_id" BIGSERIAL PRIMARY KEY,
       "permission_name" VARCHAR(100) NOT NULL,
       "permission_description" VARCHAR(255),
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "created_by" BIGINT NOT NULL,
       "updated_at" TIMESTAMP(6) WITH TIME ZONE,
       "updated_by" BIGINT,
       "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
       "deleted_by" BIGINT
);
CREATE UNIQUE INDEX "uk_permissions_name" ON "permissions" ("permission_name");

/*
    신고 도메인 DDL
    "reports",
    "report_image",
    "report_targets",
    "report_actions"
*/
-- 신고 테이블
CREATE TABLE "reports" (
       "report_id" BIGSERIAL PRIMARY KEY,
       "reporter_id" BIGINT NOT NULL,
       "reporter_uuid" UUID NOT NULL,
       "target_user_id" BIGINT NOT NULL,
       "report_reason" VARCHAR(30) NOT NULL,
       "description" TEXT,
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "created_by" BIGINT NOT NULL,
       "updated_at" TIMESTAMP(6) WITH TIME ZONE,
       "updated_by" BIGINT,
       "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
       "deleted_by" BIGINT
);
CREATE INDEX "idx_reports_reporter" ON "reports" ("reporter_id");
CREATE INDEX "idx_reports_target_user" ON "reports" ("target_user_id");
CREATE INDEX "idx_reports_reason" ON "reports" ("report_reason");

-- 신고 이미지 테이블
CREATE TABLE "report_images" (
     "report_image_id" BIGSERIAL PRIMARY KEY,
     "report_id" BIGINT NOT NULL,
     "image_url" TEXT NOT NULL
);
CREATE INDEX "idx_report_images_report" ON "report_images" ("report_id");

-- 신고 대상 테이블
CREATE TABLE "report_targets" (
      "report_target_id" BIGSERIAL PRIMARY KEY,
      "report_id" BIGINT NOT NULL,
      "target_type" VARCHAR(30) NOT NULL,
      "target_id" BIGINT NOT NULL
);
CREATE INDEX "idx_report_targets_report" ON "report_targets" ("report_id");
CREATE INDEX "idx_report_targets_target" ON "report_targets" ("target_type", "target_id");

-- 신고 처리 이력 테이블
CREATE TABLE "report_actions" (
      "report_action_id" BIGSERIAL PRIMARY KEY,
      "report_id" BIGINT NOT NULL,
      "action" VARCHAR(50) NOT NULL,
      "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
      "created_by" BIGINT NOT NULL,
      "updated_at" TIMESTAMP(6) WITH TIME ZONE,
      "updated_by" BIGINT,
      "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
      "deleted_by" BIGINT
);
CREATE INDEX "idx_report_actions_report" ON "report_actions" ("report_id");
CREATE INDEX "idx_report_actions_action" ON "report_actions" ("action");

/*
  회원 프로필 도메인 DDL
  "user_profiles",
  "user_tags",
  "user_tag_mappings"
 */
-- 회원 프로필 테이블
CREATE TABLE "user_profiles" (
     "user_profile_id" BIGSERIAL PRIMARY KEY,
     "user_id" BIGINT NOT NULL,
     "bio" VARCHAR(255),
     "birth_date" DATE,
     "gender" VARCHAR(10),
     "latitude" DECIMAL(10,7) NOT NULL,
     "longitude" DECIMAL(10,7) NOT NULL,
     "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
     "updated_at" TIMESTAMP(6) WITH TIME ZONE
);
-- users 와 1:1 보장
CREATE UNIQUE INDEX "uk_user_profiles_user" ON "user_profiles" ("user_id");
CREATE INDEX "idx_user_profiles_location" ON "user_profiles" ("latitude", "longitude");

-- 회원 태그 테이블
CREATE TABLE "user_tags" (
     "user_tag_id" BIGSERIAL PRIMARY KEY,
     "tag_name" VARCHAR(50) NOT NULL,
     "tag_category" VARCHAR(30) NOT NULL
);
CREATE UNIQUE INDEX "uk_user_tags_name" ON "user_tags" ("tag_name");
CREATE INDEX "idx_user_tags_category" ON "user_tags" ("tag_category");

-- 사용자-태그 매핑 테이블
CREATE TABLE "user_tag_mappings" (
     "user_tag_mapping_id" BIGSERIAL PRIMARY KEY,
     "user_id" BIGINT NOT NULL,
     "user_tag_id" BIGINT NOT NULL,
     "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
-- 유저-태그 중복 방지
CREATE UNIQUE INDEX "uk_user_tag_mappings_pair" ON "user_tag_mappings" ("user_id", "user_tag_id");
-- 사용자 기준 태그 조회
CREATE INDEX "idx_user_tag_mappings_user" ON "user_tag_mappings" ("user_id");
-- 태그 기준 사용자 조회
CREATE INDEX "idx_user_tag_mappings_tag" ON "user_tag_mappings" ("user_tag_id");



/*
    커뮤니티 도메인 DDL
    "communities",
    "community_tags",
    "community_members",
    "community_posts",
    "community_post_likes",
    "community_post_images",
    "community_comments",
    "community_comment_closures"
 */
-- 커뮤니티 테이블
CREATE TABLE "communities" (
       "community_id" BIGSERIAL PRIMARY KEY,
       "user_id" BIGINT NOT NULL,
       "profile_image_url" TEXT,
       "community_name" VARCHAR(50) NOT NULL,
       "description" VARCHAR(100) NOT NULL,
       "age_range" VARCHAR(10),
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "created_by" BIGINT NOT NULL,
       "updated_at" TIMESTAMP(6) WITH TIME ZONE,
       "updated_by" BIGINT,
       "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
       "deleted_by" BIGINT
);
CREATE INDEX "idx_communities_user" ON "communities" ("user_id");

-- 커뮤니티 태그 테이블
CREATE TABLE "community_tags" (
      "community_tag_id" BIGSERIAL PRIMARY KEY,
      "community_id" BIGINT NOT NULL,
      "tag_name" VARCHAR(12) NOT NULL
);

CREATE INDEX "idx_community_tags_community" ON "community_tags" ("community_id");
CREATE UNIQUE INDEX "uk_community_tags_community_tag" ON "community_tags" ("community_id", "tag_name");


-- 커뮤니티 회원 테이블
CREATE TABLE "community_members" (
     "community_member_id" BIGSERIAL PRIMARY KEY,
     "community_id" BIGINT NOT NULL,
     "user_id" BIGINT NOT NULL,
     "joined_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX "idx_community_members_user" ON "community_members" ("user_id");
CREATE INDEX "idx_community_members_community" ON "community_members" ("community_id");

-- 커뮤니티 포스트 테이블
CREATE TABLE "community_posts" (
       "post_id" BIGSERIAL PRIMARY KEY,
       "community_id" BIGINT NOT NULL,
       "author_id" BIGINT NOT NULL,
       "title" VARCHAR(100) NOT NULL,
       "content" TEXT NOT NULL,
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "created_by" BIGINT NOT NULL,
       "updated_at" TIMESTAMP(6) WITH TIME ZONE,
       "updated_by" BIGINT,
       "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
       "deleted_by" BIGINT
);
CREATE INDEX "idx_posts_community" ON "community_posts" ("community_id");

-- 커뮤니티 포스트 좋아요 테이블
CREATE TABLE "community_post_likes" (
    "community_post_like_id" BIGSERIAL PRIMARY KEY,
    "post_id" BIGINT NOT NULL,
    "user_id" BIGINT NOT NULL
);
-- 게시글 좋아요 수 집계
CREATE INDEX "idx_community_post_likes_post" ON "community_post_likes" ("post_id");
-- 유저가 누른 좋아요 조회
CREATE INDEX "idx_community_post_likes_user" ON "community_post_likes" ("user_id");
-- 유저가 같은 게시글에 중복 좋아요 방지
CREATE UNIQUE INDEX "uk_community_post_likes_user_post" ON "community_post_likes" ("post_id", "user_id");


-- 커뮤니티 포스트 이미지 테이블
CREATE TABLE "community_post_images" (
     "community_post_image_id" BIGSERIAL PRIMARY KEY,
     "post_id" BIGINT NOT NULL,
     "image_url" TEXT NOT NULL
);
CREATE INDEX "idx_community_post_images_post" ON "community_post_images" ("post_id");

-- 커뮤니티 포스트 댓글 테이블
CREATE TABLE "community_comments" (
      "comment_id" BIGSERIAL PRIMARY KEY,
      "post_id" BIGINT NOT NULL,
      "author_id" BIGINT NOT NULL,
      "content" TEXT NOT NULL
);
CREATE INDEX "idx_comments_post" ON "community_comments" ("post_id");

-- 커뮤니티 포스트 댓글 클로저 테이블
CREATE TABLE "community_comment_closures" (
      "ancestor_id" BIGINT NOT NULL,
      "descendant_id" BIGINT NOT NULL,
      "depth" INTEGER NOT NULL,
      PRIMARY KEY ("ancestor_id", "descendant_id")
);
CREATE INDEX "idx_comment_closure_ancestor" ON "community_comment_closures" ("ancestor_id");
CREATE INDEX "idx_comment_closure_descendant" ON "community_comment_closures" ("descendant_id");


/*
    매칭 도메인
    "matches",
    "user_match_excludes"
*/
-- 매칭 테이블
CREATE TABLE "matches" (
       "match_id" BIGSERIAL PRIMARY KEY,
       "requester_id" BIGINT NOT NULL,
       "receiver_id" BIGINT NOT NULL,
       "status" VARCHAR(30) NOT NULL,
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "created_by" BIGINT NOT NULL,
       "updated_at" TIMESTAMP(6) WITH TIME ZONE,
       "updated_by" BIGINT,
       "deleted_at" TIMESTAMP(6) WITH TIME ZONE,
       "deleted_by" BIGINT
);
CREATE INDEX "idx_matches_requester" ON "matches" ("requester_id");
CREATE INDEX "idx_matches_receiver" ON "matches" ("receiver_id");
CREATE INDEX "idx_matches_status" ON "matches" ("status");

-- 매칭 제외 테이블
CREATE TABLE "user_match_excludes" (
       "user_match_excludes_id" BIGSERIAL PRIMARY KEY,
       "user_id" BIGINT NOT NULL,
       "excluded_user_id" BIGINT NOT NULL,
       "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE UNIQUE INDEX "uk_user_match_excludes_pair" ON "user_match_excludes" ("user_id", "excluded_user_id");
CREATE INDEX "idx_user_match_excludes_user" ON "user_match_excludes" ("user_id");
CREATE INDEX "idx_user_match_excludes_excluded_user" ON "user_match_excludes" ("excluded_user_id");


/*
    채팅 도메인 DDL
    "chat_rooms",
    "chat_room_users",
    "chat_messages",
    "video_calls",
    "video_verifications"
 */
-- 채팅방 테이블
CREATE TABLE "chat_rooms" (
      "chat_room_id" BIGSERIAL PRIMARY KEY,
      "match_id" BIGINT NOT NULL,
      "first_user_id" BIGINT NOT NULL,
      "second_user_id" BIGINT NOT NULL,
      "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX "idx_chat_rooms_match" ON "chat_rooms" ("match_id");
CREATE INDEX "idx_chat_rooms_first_user" ON "chat_rooms" ("first_user_id");
CREATE INDEX "idx_chat_rooms_second_user" ON "chat_rooms" ("second_user_id");


-- 나가기 처리 테이블
CREATE TABLE "chat_room_users" (
       "chat_room_user_id" BIGSERIAL PRIMARY KEY,
       "chat_room_id" BIGINT NOT NULL,
       "user_id" BIGINT NOT NULL,
       "is_left" BOOLEAN NOT NULL DEFAULT FALSE,
       "left_at" TIMESTAMP(6) WITH TIME ZONE
);
CREATE INDEX "idx_chat_room_users_room" ON "chat_room_users" ("chat_room_id");
CREATE INDEX "idx_chat_room_users_user" ON "chat_room_users" ("user_id");


-- 채팅 메시지 테이블
CREATE TABLE "chat_messages" (
     "chat_message_id" BIGSERIAL PRIMARY KEY,
     "chat_room_id" BIGINT NOT NULL,
     "sender_id" BIGINT NOT NULL,
     "nickname" VARCHAR(20) NOT NULL,
     "content" TEXT NOT NULL,
     "created_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX "idx_chat_messages_room" ON "chat_messages" ("chat_room_id");
CREATE INDEX "idx_chat_messages_sender" ON "chat_messages" ("sender_id");
CREATE INDEX "idx_chat_messages_created_at" ON "chat_messages" ("created_at");


-- 화상통화 테이블
CREATE TABLE "video_calls" (
       "video_call_id" BIGSERIAL PRIMARY KEY,
       "chat_room_id" BIGINT NOT NULL,
       "started_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "ended_at" TIMESTAMP(6) WITH TIME ZONE
);
CREATE INDEX "idx_video_calls_chat_room" ON "video_calls" ("chat_room_id");
CREATE INDEX "idx_video_calls_started_at" ON "video_calls" ("started_at");


-- 화상통화 인증 테이블
CREATE TABLE "video_verifications" (
       "video_verification_id" BIGSERIAL PRIMARY KEY,
       "user_id" BIGINT NOT NULL,
       "is_verified" BOOLEAN NOT NULL,
       "verified_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL,
       "expires_at" TIMESTAMP(6) WITH TIME ZONE NOT NULL
);
CREATE INDEX "idx_video_verifications_user" ON "video_verifications" ("user_id");
CREATE INDEX "idx_video_verifications_verified" ON "video_verifications" ("is_verified");
CREATE INDEX "idx_video_verifications_expires_at" ON "video_verifications" ("expires_at");


