/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 160004 (160004)
 Source Host           : localhost:15432
 Source Catalog        : image
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160004 (160004)
 File Encoding         : 65001

 Date: 02/10/2024 23:10:46
*/

/*
 Target Database: image_hosting
 Description: 核心图片资源库，负责存储图片的物理属性、元数据、统计信息及MinIO路径。
*/

DROP DATABASE IF EXISTS "image_hosting";
CREATE DATABASE "image_hosting";

-- --------------------------------------------------------
-- 1. 配置表 (Config)
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."config";
CREATE TABLE "public"."config" (
                                   "config_id" varchar(100) NOT NULL,
                                   "user_id" varchar(100),
                                   "config_key" varchar(50) NOT NULL,
                                   "config_value" varchar(255),
                                   "is_delete" bool NOT NULL DEFAULT false,
                                   "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                   "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT "config_pkey" PRIMARY KEY ("config_id")
);

-- 注释
COMMENT ON TABLE "public"."config" IS '系统和用户配置表，用于存储开关或参数';
COMMENT ON COLUMN "public"."config"."config_id" IS '配置ID (主键)';
COMMENT ON COLUMN "public"."config"."user_id" IS '用户ID (NULL表示全局配置)';
COMMENT ON COLUMN "public"."config"."config_key" IS '配置键名 (e.g., ORIGINAL_IMAGE_PROTECTION)';
COMMENT ON COLUMN "public"."config"."config_value" IS '配置值';
COMMENT ON COLUMN "public"."config"."is_delete" IS '是否删除 (软删除)';
COMMENT ON COLUMN "public"."config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."config"."update_time" IS '更新时间';

-- 索引
CREATE UNIQUE INDEX "config_key_index" ON "public"."config"("config_key", "user_id");

INSERT INTO "public"."config" VALUES ('1', NULL, 'ORIGINAL_IMAGE_PROTECTION', '1', 'f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO "public"."config" VALUES ('2', NULL, 'ORIGINAL_WEBP_CONVERSION', '1', 'f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- --------------------------------------------------------
-- 2. 图片数据表 (Image Data) - 核心增强
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."image_data";
CREATE TABLE "public"."image_data" (
                                       "image_id" varchar(100) NOT NULL,
                                       "user_id" varchar(100) NOT NULL,        -- 上传者ID
                                       "nft_id" varchar(255),
                                       "token_id" varchar(255),
                                       "file_name" varchar(100) NOT NULL,      -- 原始文件名
                                       "size" int8 NOT NULL,                   -- 文件大小 (Bytes)
                                       "content_type" varchar(50) NOT NULL,    -- MIME类型 (image/jpeg)
                                       "file_hash" varchar(64) NOT NULL,       -- SHA256哈希值

-- [存储路径区分]
                                       "origin_minio_key" varchar(255) NOT NULL,   -- 原图MinIO中的存储对象Key (由 minio_key 重命名)
                                       "origin_minio_url" varchar(255),            -- 原图内部路径 (私有桶，需授权访问)

                                       "watermark_minio_key" varchar(255),         -- 水印图/预览图MinIO Key (新增)
                                       "watermark_minio_url" varchar(255),         -- 水印图/预览图路径 (公共桶，公开访问)

                                       "thumbnail_minio_key" varchar(255),         -- 缩略图MinIO Key (新增)
                                       "thumbnail_minio_url" varchar(255),         -- 缩略图公共访问URL (新增)

    -- [基本信息]
                                       "description" text,                         -- 图片描述
                                       "is_public" bool NOT NULL DEFAULT false,     -- 是否公开展示
                                       "audit_status" int2 DEFAULT 0,              -- 审核状态: 0-待审, 1-通过, 2-拒绝
                                       "audit_msg" varchar(255),                   -- 审核备注/拒绝原因

    -- [EXIF 元数据 - 摄影参数]
                                       "camera_make" varchar(50),                  -- 相机厂商 (Canon)
                                       "camera_model" varchar(50),                 -- 相机型号 (EOS R5)
                                       "lens_model" varchar(100),                  -- 镜头型号
                                       "focal_length" varchar(20),                 -- 焦距
                                       "aperture" varchar(20),                     -- 光圈 (f/2.8)
                                       "shutter_speed" varchar(20),                -- 快门 (1/200s)
                                       "iso" int4,                                 -- ISO 感光度
                                       "shoot_time" timestamp(6),                  -- 实际拍摄时间 (从Exif读取)
                                       "width" int8,                               -- 宽 (像素)
                                       "height" int8,                              -- 高 (像素)


    -- [地理位置]
                                       "location_name" varchar(100),               -- 地点名称
                                       "latitude" decimal(10, 7),                  -- 纬度
                                       "longitude" decimal(10, 7),                 -- 经度

    -- [统计与分类]
                                       "view_count" int8 DEFAULT 0,                -- 浏览量
                                       "download_count" int8 DEFAULT 0,            -- 下载量
                                       "like_count" int8 DEFAULT 0,                -- 点赞/收藏数
                                       "category" varchar(50),                     -- 分类 (风景, 人像)
                                       "dominant_color" varchar(7),                -- 主色调 (e.g. #3366CC)

    -- [系统字段]
                                       "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "is_delete" bool NOT NULL DEFAULT false,    -- 软删除标记

                                       CONSTRAINT "image_data_pkey" PRIMARY KEY ("image_id")
);

-- 注释
COMMENT ON TABLE "public"."image_data" IS '图片核心数据表，存储文件信息、元数据和统计';
COMMENT ON COLUMN "public"."image_data"."image_id" IS '图片唯一ID (主键)';
COMMENT ON COLUMN "public"."image_data"."user_id" IS '上传者用户ID';
COMMENT ON COLUMN "public"."image_data"."file_name" IS '原始文件名';
COMMENT ON COLUMN "public"."image_data"."size" IS '文件大小 (Bytes)';
COMMENT ON COLUMN "public"."image_data"."content_type" IS 'MIME类型 (e.g., image/jpeg)';
COMMENT ON COLUMN "public"."image_data"."file_hash" IS 'SHA256哈希值';
COMMENT ON COLUMN "public"."image_data"."nft_id" IS '区块链系统生成id';
COMMENT ON COLUMN "public"."image_data"."token_id" IS '区块链智能合约生成, NFT核心身份标识';
-- MinIO Key/URL 注释更新
COMMENT ON COLUMN "public"."image_data"."origin_minio_key" IS '原图的MinIO存储对象Key (高清)';
COMMENT ON COLUMN "public"."image_data"."origin_minio_url" IS '高清原图的MinIO内部路径，需授权访问';
COMMENT ON COLUMN "public"."image_data"."watermark_minio_key" IS '带水印预览图的MinIO存储对象Key';
COMMENT ON COLUMN "public"."image_data"."watermark_minio_url" IS '带水印预览图的公共访问URL';
COMMENT ON COLUMN "public"."image_data"."thumbnail_minio_key" IS '缩略图的MinIO存储对象Key';
COMMENT ON COLUMN "public"."image_data"."thumbnail_minio_url" IS '缩略图的公共访问URL';
COMMENT ON COLUMN "public"."image_data"."description" IS '图片描述';
COMMENT ON COLUMN "public"."image_data"."is_public" IS '是否公开展示';
COMMENT ON COLUMN "public"."image_data"."audit_status" IS '审核状态 (0-待审, 1-通过, 2-拒绝)';
COMMENT ON COLUMN "public"."image_data"."audit_msg" IS '审核备注/拒绝原因';
COMMENT ON COLUMN "public"."image_data"."camera_make" IS '相机厂商 (e.g., Canon, Sony)';
COMMENT ON COLUMN "public"."image_data"."camera_model" IS '相机型号';
COMMENT ON COLUMN "public"."image_data"."lens_model" IS '镜头型号';
COMMENT ON COLUMN "public"."image_data"."focal_length" IS '焦距 (e.g., 50mm)';
COMMENT ON COLUMN "public"."image_data"."aperture" IS '光圈值 (e.g., f/2.8)';
COMMENT ON COLUMN "public"."image_data"."shutter_speed" IS '快门速度 (e.g., 1/200s)';
COMMENT ON COLUMN "public"."image_data"."iso" IS 'ISO 感光度';
COMMENT ON COLUMN "public"."image_data"."shoot_time" IS '实际拍摄时间 (从Exif读取)';
COMMENT ON COLUMN "public"."image_data"."width" IS '宽 (像素)';
COMMENT ON COLUMN "public"."image_data"."height" IS '高 (像素)';
COMMENT ON COLUMN "public"."image_data"."location_name" IS '地理位置名称';
COMMENT ON COLUMN "public"."image_data"."latitude" IS '纬度';
COMMENT ON COLUMN "public"."image_data"."longitude" IS '经度';
COMMENT ON COLUMN "public"."image_data"."view_count" IS '图片浏览量';
COMMENT ON COLUMN "public"."image_data"."download_count" IS '高清原图下载次数';
COMMENT ON COLUMN "public"."image_data"."like_count" IS '点赞/收藏数';
COMMENT ON COLUMN "public"."image_data"."category" IS '图片分类 (e.g., 风景, 人像)';
COMMENT ON COLUMN "public"."image_data"."dominant_color" IS '主色调 (HEX值，用于前端占位)';
COMMENT ON COLUMN "public"."image_data"."create_time" IS '记录创建时间 (上传时间)';
COMMENT ON COLUMN "public"."image_data"."update_time" IS '记录更新时间';
COMMENT ON COLUMN "public"."image_data"."is_delete" IS '是否删除 (软删除)';

-- 索引优化
CREATE INDEX "image_data_user_id_index" ON "public"."image_data"("user_id");
CREATE INDEX "image_data_is_public_index" ON "public"."image_data"("is_public");
CREATE INDEX "image_data_create_time_index" ON "public"."image_data"("create_time" DESC);
CREATE INDEX "image_data_category_index" ON "public"."image_data"("category");
CREATE INDEX "image_data_view_count_index" ON "public"."image_data"("view_count" DESC);
CREATE INDEX "image_data_shoot_time_index" ON "public"."image_data"("shoot_time" DESC);
CREATE INDEX "image_data_is_delete_index" ON "public"."image_data"("is_delete");

CREATE UNIQUE INDEX "image_data_origin_minio_key_uindex" ON "public"."image_data"("origin_minio_key");
CREATE INDEX "image_data_origin_minio_url_index" ON "public"."image_data"("origin_minio_url");

CREATE UNIQUE INDEX "image_data_watermark_minio_key_uindex" ON "public"."image_data"("watermark_minio_key");
CREATE INDEX "image_data_watermark_minio_url_index" ON "public"."image_data"("watermark_minio_url");

CREATE UNIQUE INDEX "image_data_thumbnail_minio_key_uindex" ON "public"."image_data"("thumbnail_minio_key");
CREATE INDEX "image_data_thumbnail_minio_url_index" ON "public"."image_data"("thumbnail_minio_url");

CREATE UNIQUE INDEX "image_data_file_hash_uindex" ON "public"."image_data"("file_hash");
CREATE UNIQUE INDEX "image_data_nft_id_uindex" ON "public"."image_data"("nft_id");
CREATE UNIQUE INDEX "image_data_token_id_uindex" ON "public"."image_data"("token_id");

-- --------------------------------------------------------
-- 3. 存储策略表 (Strategies)
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."strategies";
CREATE TABLE "public"."strategies" (
                                       "id" varchar(255) NOT NULL,
                                       "user_id" varchar(255),
                                       "type" varchar(255) NOT NULL, -- 策略类型 (e.g., WATERMARK, CONVERSION)
                                       "config" json NOT NULL,       -- 策略配置JSON (e.g., 水印位置, 转换质量)
                                       "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       "is_delete" bool DEFAULT false,
                                       CONSTRAINT "strategies_pkey" PRIMARY KEY ("id")
);

-- 注释
COMMENT ON TABLE "public"."strategies" IS '图片处理策略表 (水印, 压缩, 格式转换等)';
COMMENT ON COLUMN "public"."strategies"."id" IS '策略ID (主键)';
COMMENT ON COLUMN "public"."strategies"."user_id" IS '策略所属用户ID (NULL为系统默认策略)';
COMMENT ON COLUMN "public"."strategies"."type" IS '策略类型 (e.g., WATERMARK, COMPRESSION)';
COMMENT ON COLUMN "public"."strategies"."config" IS 'JSON格式的策略详细配置';
COMMENT ON COLUMN "public"."strategies"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."strategies"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."strategies"."is_delete" IS '是否删除 (软删除)';

-- 索引
CREATE INDEX "strategies_user_id_index" ON "public"."strategies"("user_id");
CREATE INDEX "strategies_type_index" ON "public"."strategies"("type");


-- --------------------------------------------------------
-- 4. 用户基础表 (User Info for Image Hosting) - Enhanced
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."user_info";
CREATE TABLE "public"."user_info" (
    -- 核心身份认证
                                      "user_id" varchar(100) NOT NULL,
                                      "user_name" varchar(50) NOT NULL UNIQUE,  -- 登录账号 (唯一，英文+数字)
                                      "password" varchar(255) NOT NULL,         -- 密码哈希
                                      "user_email" varchar(100) NOT NULL UNIQUE,-- 绑定邮箱
                                      "phone_number" varchar(20),               -- 绑定手机 (可选)

    -- 个人资料展示 (Profile)
                                      "nickname" varchar(50),                   -- 显示昵称 (支持中文，可重复)
                                      "avatar_url" varchar(500),                -- 头像链接 (MinIO URL)
                                      "bio" varchar(500),                       -- 个人简介/签名
                                      "website_url" varchar(255),               -- 个人网站/作品集链接

    -- 业务限制与统计 (Business Logic)
                                      "user_role" varchar(20) DEFAULT 'user',   -- 角色: admin, user, vip
                                      "status" int2 DEFAULT 1,                  -- 状态: 1-正常, 0-禁用/封禁, 2-未激活
--                                       "storage_limit" int8 DEFAULT 1073741824,  -- 存储配额 (单位: 字节, 默认1GB)
--                                       "storage_used" int8 DEFAULT 0,            -- 已用存储 (单位: 字节)

    -- Web3 关联
                                      "blockchain_address" varchar(255) UNIQUE, -- 关联的钱包地址
                                      "blockchain_status" int2 DEFAULT 0,       -- 区块链注册状态: 1-Active, 0-Deregistered

    -- 系统审计
                                      "last_login_time" timestamp(6),           -- 最后登录时间
                                      "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                      "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                      "is_delete" bool NOT NULL DEFAULT false,

                                      CONSTRAINT "user_info_pkey" PRIMARY KEY ("user_id")
);

-- ----------------------------
-- 注释 (Comments)
-- ----------------------------
COMMENT ON TABLE "public"."user_info" IS '图床系统的用户基础信息表';

COMMENT ON COLUMN "public"."user_info"."user_id" IS '用户唯一ID (UUID)';
COMMENT ON COLUMN "public"."user_info"."user_name" IS '登录用户名';
COMMENT ON COLUMN "public"."user_info"."nickname" IS '前台展示昵称';
COMMENT ON COLUMN "public"."user_info"."avatar_url" IS '用户头像URL';
COMMENT ON COLUMN "public"."user_info"."bio" IS '个人简介';
COMMENT ON COLUMN "public"."user_info"."storage_limit" IS '存储空间上限(Byte)';
COMMENT ON COLUMN "public"."user_info"."storage_used" IS '已使用存储空间(Byte)';
COMMENT ON COLUMN "public"."user_info"."status" IS '账号状态: 1-Active, 0-Banned';
COMMENT ON COLUMN "public"."user_info"."blockchain_address" IS 'Web3钱包地址';

-- ----------------------------
-- 索引 (Indexes)
-- ----------------------------
CREATE UNIQUE INDEX "user_info_blockchain_address_idx" ON "public"."user_info"("blockchain_address");
CREATE INDEX "user_info_email_idx" ON "public"."user_info"("user_email");
CREATE INDEX "user_info_status_idx" ON "public"."user_info"("status");
CREATE INDEX "user_info_isdelete_idx" ON "public"."user_info"("is_delete");

-- ------------------------------------------------
-- 5. 用户统计表 (User Stats)
-- ------------------------------------------------
DROP TABLE IF EXISTS "public"."user_stats";
CREATE TABLE "public"."user_stats" (
                                       "user_id" varchar(100) NOT NULL,
                                       "total_uploads" int4 DEFAULT 0,            -- 总上传图片数
                                       "total_views" int8 DEFAULT 0,              -- 图片总浏览量
                                       "total_downloads" int8 DEFAULT 0,          -- 图片总点击下载量 (新增)
                                       "total_likes" int8 DEFAULT 0,              -- 图片总获赞数
                                       "storage_limit" int8 DEFAULT 1073741824,   -- 存储配额 (字节, 默认1GB)
                                       "storage_used" int8 DEFAULT 0,             -- 已用存储 (字节)
                                       "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT "user_stats_pkey" PRIMARY KEY ("user_id")
);

-- 注释
COMMENT ON TABLE "public"."user_stats" IS '图床侧用户数据统计和成就表';
COMMENT ON COLUMN "public"."user_stats"."user_id" IS '用户唯一ID (主键)';
COMMENT ON COLUMN "public"."user_stats"."total_uploads" IS '累计上传图片数量';
COMMENT ON COLUMN "public"."user_stats"."total_views" IS '所有图片的累计浏览量';
COMMENT ON COLUMN "public"."user_stats"."total_downloads" IS '所有图片的累计下载量';
COMMENT ON COLUMN "public"."user_stats"."total_likes" IS '所有图片的累计点赞数';
COMMENT ON COLUMN "public"."user_stats"."storage_limit" IS '存储空间配额 (Bytes)';
COMMENT ON COLUMN "public"."user_stats"."storage_used" IS '已使用的存储空间 (Bytes)';
COMMENT ON COLUMN "public"."user_stats"."update_time" IS '统计数据更新时间';

-- 索引 (用于排行或高性能审计)
CREATE INDEX "user_stats_total_views_index" ON "public"."user_stats"("total_views" DESC);
CREATE INDEX "user_stats_total_uploads_index" ON "public"."user_stats"("total_uploads" DESC);
CREATE INDEX "user_stats_total_likes_index" ON "public"."user_stats"("total_likes" DESC);


-- --------------------------------------------------------
-- 6. 收藏表
-- --------------------------------------------------------

DROP TABLE IF EXISTS "public"."favorites";
CREATE TABLE "public"."favorites" (
                                      "favorite_id" varchar(100) NOT NULL,
                                      "user_id" varchar(100) NOT NULL,
                                      "image_id" varchar(100) NOT NULL,
                                      "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                      "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                      "is_delete" bool NOT NULL DEFAULT false,
                                      CONSTRAINT "favorites_pkey" PRIMARY KEY ("favorite_id")
);

-- 注释
COMMENT ON TABLE "public"."favorites" IS '用户收藏表';
COMMENT ON COLUMN "public"."favorites"."favorite_id" IS '收藏ID (主键)';
COMMENT ON COLUMN "public"."favorites"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."favorites"."image_id" IS '图片ID';
COMMENT ON COLUMN "public"."favorites"."create_time" IS '收藏时间';
COMMENT ON COLUMN "public"."favorites"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."favorites"."is_delete" IS '是否删除';

-- 索引
CREATE UNIQUE INDEX "favorites_user_image_unique" ON "public"."favorites"("user_id", "image_id");
CREATE INDEX "favorites_user_id_index" ON "public"."favorites"("user_id");
CREATE INDEX "favorites_create_time_index" ON "public"."favorites"("create_time" DESC);
CREATE INDEX favorites_is_delete_index ON public.favorites(is_delete);


-- --------------------------------------------------------
-- Notices Table
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."notices";
CREATE TABLE "public"."notices" (
                                    "notice_id" varchar(100) NOT NULL,
                                    "title" varchar(255) NOT NULL,
                                    "content" text NOT NULL,
                                    "publisher_id" varchar(100) NOT NULL,
                                    "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                    "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                    "is_delete" boolean DEFAULT false,
                                    CONSTRAINT "notices_pkey" PRIMARY KEY ("notice_id")
);

COMMENT ON TABLE "public"."notices" IS 'System notices and announcements';
COMMENT ON COLUMN "public"."notices"."notice_id" IS 'Notice ID (Primary Key)';
COMMENT ON COLUMN "public"."notices"."title" IS 'Notice Title';
COMMENT ON COLUMN "public"."notices"."content" IS 'Notice Content';
COMMENT ON COLUMN "public"."notices"."publisher_id" IS 'ID of the user who published the notice';
COMMENT ON COLUMN "public"."notices"."create_time" IS 'Creation time';
COMMENT ON COLUMN "public"."notices"."update_time" IS 'Update time';
COMMENT ON COLUMN "public"."notices"."is_delete" IS 'Soft delete flag';

-- Indexes
CREATE INDEX "notices_create_time_index" ON "public"."notices"("create_time" DESC);
CREATE INDEX "notices_is_delete_index" ON "public"."notices"("is_delete");


-- --------------------------------------------------------
-- Comments Table
-- --------------------------------------------------------
DROP TABLE IF EXISTS "public"."comments";
CREATE TABLE "public"."comments" (
                                     "comment_id" varchar(100) NOT NULL,
                                     "image_id" varchar(100) NOT NULL,
                                     "user_id" varchar(100) NOT NULL,
                                     "user_name" varchar(100) NOT NULL,
                                     "content" text NOT NULL,
                                     "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "update_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
                                     "is_delete" boolean DEFAULT false,
                                     CONSTRAINT "comments_pkey" PRIMARY KEY ("comment_id")
);

COMMENT ON TABLE "public"."comments" IS 'Image comments table';
COMMENT ON COLUMN "public"."comments"."comment_id" IS 'Comment ID (Primary Key)';
COMMENT ON COLUMN "public"."comments"."image_id" IS 'Image ID';
COMMENT ON COLUMN "public"."comments"."user_id" IS 'User ID';
COMMENT ON COLUMN "public"."comments"."user_name" IS 'User Name';
COMMENT ON COLUMN "public"."comments"."content" IS 'Comment Content';
COMMENT ON COLUMN "public"."comments"."create_time" IS 'Creation time';
COMMENT ON COLUMN "public"."comments"."update_time" IS 'Update time';
COMMENT ON COLUMN "public"."comments"."is_delete" IS 'Soft delete flag';

-- Indexes
CREATE INDEX "comments_image_id_index" ON "public"."comments"("image_id");
CREATE INDEX "comments_user_id_index" ON "public"."comments"("user_id");
CREATE INDEX "comments_create_time_index" ON "public"."comments"("create_time" DESC);
CREATE INDEX "comments_is_delete_index" ON "public"."comments"("is_delete");

-- --------------------------------------------------------
-- Downloads Table
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS downloads (
                                         download_id VARCHAR(64) PRIMARY KEY,
                                         user_id VARCHAR(64),
                                         image_id VARCHAR(64) NOT NULL,
                                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         is_deleted INT DEFAULT 0
);

COMMENT ON TABLE downloads IS '图片下载记录表';
COMMENT ON COLUMN downloads.download_id IS '下载ID';
COMMENT ON COLUMN downloads.user_id IS '用户ID';
COMMENT ON COLUMN downloads.image_id IS '图片ID';

-- --------------------------------------------------------
-- 6. 触发器机制 (Triggers for automatic update_time)
-- --------------------------------------------------------

-- 触发器函数：自动设置 update_time 字段为当前时间
CREATE EXTENSION IF NOT EXISTS plpgsql;

CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 应用于 config 表
DROP TRIGGER IF EXISTS trg_config_update_time ON "public"."config";
CREATE TRIGGER trg_config_update_time
    BEFORE UPDATE ON "public"."config"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 image_data 表
DROP TRIGGER IF EXISTS trg_image_data_update_time ON "public"."image_data";
CREATE TRIGGER trg_image_data_update_time
    BEFORE UPDATE ON "public"."image_data"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 strategies 表
DROP TRIGGER IF EXISTS trg_strategies_update_time ON "public"."strategies";
CREATE TRIGGER trg_strategies_update_time
    BEFORE UPDATE ON "public"."strategies"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 user_info 表
DROP TRIGGER IF EXISTS trg_user_info_update_time ON "public"."user_info";
CREATE TRIGGER trg_user_info_update_time
    BEFORE UPDATE ON "public"."user_info"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 user_stats 表
DROP TRIGGER IF EXISTS trg_user_stats_update_time ON "public"."user_stats";
CREATE TRIGGER trg_user_stats_update_time
    BEFORE UPDATE ON "public"."user_stats"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 favorites 表
DROP TRIGGER IF EXISTS trg_favorites_update_time ON "public"."favorites";
CREATE TRIGGER trg_favorites_update_time
    BEFORE UPDATE ON "public"."favorites"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 notices 表
DROP TRIGGER IF EXISTS trg_notices_update_time ON "public"."notices";
CREATE TRIGGER trg_notices_update_time
    BEFORE UPDATE ON "public"."notices"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();

-- 应用于 comments 表
DROP TRIGGER IF EXISTS trg_comments_update_time ON "public"."comments";
CREATE TRIGGER trg_comments_update_time
    BEFORE UPDATE ON "public"."comments"
    FOR EACH ROW
EXECUTE FUNCTION update_modified_column();