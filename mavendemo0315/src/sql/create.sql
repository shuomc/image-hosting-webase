-- 传统用户表 (Modified to include blockchain address and role linkage)
-- Adapting from your user_info table
DROP TABLE IF EXISTS "public"."app_user";
CREATE TABLE "public"."app_user" (
                                     "user_id" VARCHAR(100) NOT NULL, -- Traditional application user ID (Primary Key)
                                     "user_name" VARCHAR(50) NOT NULL UNIQUE, -- Username
                                     "password_hash" VARCHAR(255) NOT NULL, -- Hashed password
                                     "user_email" VARCHAR(100) NOT NULL UNIQUE, -- User email
                                     "blockchain_address" VARCHAR(255) UNIQUE, -- !!! 新增字段: 用户对应的区块链地址 !!!
                                     "encrypted_private_key" VARCHAR(255) UNIQUE,
    -- Used to link user to on-chain ownership
                                     "role_id" INT NOT NULL,             -- !!! 新增字段: 关联到权限表 !!!
                                     "create_time" TIMESTAMP,
                                     "update_time" TIMESTAMP,
                                     "is_delete" BOOLEAN NOT NULL DEFAULT FALSE, -- Soft delete flag
                                     PRIMARY KEY ("user_id")
);
COMMENT ON TABLE "public"."app_user" IS '应用用户表，关联区块链地址';
COMMENT ON COLUMN "public"."app_user"."blockchain_address" IS '用户在区块链上的地址，用于链上所有权关联';
COMMENT ON COLUMN "public"."app_user"."role_id" IS '关联到用户角色权限表';

-- 权限角色表 (新增)
DROP TABLE IF EXISTS "public"."app_role";
CREATE TABLE "public"."app_role" (
                                     "role_id" SERIAL PRIMARY KEY, -- Role ID (Primary Key)
                                     "role_name" VARCHAR(50) NOT NULL UNIQUE, -- Role name (e.g., 'admin', 'user')
                                     "description" VARCHAR(255) -- Role description
);
COMMENT ON TABLE "public"."app_role" IS '应用角色权限表';

-- 艺术品资产表 (Modified to include CID and blockchain token ID)
-- Adapting from your image_data table
DROP TABLE IF EXISTS "public"."artwork_asset";
CREATE TABLE "public"."artwork_asset" (
                                          "asset_id" VARCHAR(100) NOT NULL, -- 传统应用内部的资产ID (Primary Key, mapping from original image_id)
                                          "asset_cid" VARCHAR(255) NOT NULL UNIQUE, -- !!! 修改字段: 文件内容的哈希值 (CID) !!!
    -- Used as immutable content identifier and MinIO key
                                          "token_id" VARCHAR(255) UNIQUE,     -- !!! 新增字段: 链上 Token ID !!!
    -- The unique ID assigned by the blockchain smart contract (e.g., NFT ID)
    -- NULL initially, populated after successful minting
                                          "creator_user_id" VARCHAR(100) NOT NULL, -- 传统应用用户ID (Foreign Key to app_user) - 记录上传者
                                          "file_name" VARCHAR(100) NOT NULL,    -- Original file name
                                          "content_type" VARCHAR(50) NOT NULL,  -- MIME type
                                          "size" BIGINT NOT NULL,               -- File size in bytes
                                          "width" INT,                          -- Image width
                                          "height" INT,                         -- Image height
                                          "description" TEXT,                   -- Asset description
                                          "metadata_uri" VARCHAR(255),          -- !!! 新增字段: 链下元数据URI (如果使用) !!!
    -- Points to off-chain metadata JSON file
                                          "minio_storage_key" VARCHAR(255) NOT NULL UNIQUE, -- !!! 修改字段: 实际MinIO存储Key !!!
    -- Ideally, this IS the asset_cid, but kept separate for clarity if needed.
    -- If asset_cid is always used as key, this is redundant or can be removed.
                                          "is_public" BOOLEAN NOT NULL DEFAULT TRUE,  -- Application-level visibility
                                          "is_delete" BOOLEAN NOT NULL DEFAULT FALSE, -- Application-level soft delete
                                          "upload_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Time asset was uploaded and record created
                                          "mint_time" TIMESTAMP,                -- !!! 新增字段: 成功铸造到链上的时间 !!!
                                          PRIMARY KEY ("asset_id"),
                                          FOREIGN KEY ("creator_user_id") REFERENCES "public"."app_user"("user_id")
    -- Add index on asset_cid, token_id, creator_user_id for efficient lookups
);
COMMENT ON TABLE "public"."artwork_asset" IS '数字艺术品资产表，关联链上Token';
COMMENT ON COLUMN "public"."artwork_asset"."asset_cid" IS '文件内容的哈希值 (CID)，作为内容标识符和MinIO存储Key';
COMMENT ON COLUMN "public"."artwork_asset"."token_id" IS '关联的链上数字藏品 Token ID';
COMMENT ON COLUMN "public"."artwork_asset"."creator_user_id" IS '资产的创建/上传用户ID';
COMMENT ON COLUMN "public"."artwork_asset"."metadata_uri" IS '指向链下元数据JSON文件的URI';
COMMENT ON COLUMN "public"."artwork_asset"."minio_storage_key" IS '文件在MinIO中的存储Key，应与asset_cid一致';
COMMENT ON COLUMN "public"."artwork_asset"."mint_time" IS '资产成功铸造到链上的时间';


-- Note: The 'config' and 'strategies' tables from your original schema seem related to application configuration and specific processing strategies,
-- which typically remain in the traditional database and are not directly related to the core blockchain ownership logic.
-- So, 'config' and 'strategies' tables can be kept as they are or adapted based on your application's needs.

INSERT INTO app_role (role_name, description) VALUES
    ('admin', 'Administrator role with full access and privileges'),
    ('user', 'Default role for regular users');

-- ----------------------------
-- Table structure for nft_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."nft_info";
CREATE TABLE "public"."nft_info" (
    "nft_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "image_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "minio_url" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "blockchain_address" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "token_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "contract_address" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "price" decimal(20,8),
    "description" TEXT,
    "is_for_sale" bool NOT NULL DEFAULT true,
    "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_time" timestamp(6),
    "is_delete" bool NOT NULL DEFAULT false,
    CONSTRAINT "nft_info_pkey" PRIMARY KEY ("nft_id"),
    CONSTRAINT "nft_info_owner_id_fkey" FOREIGN KEY ("blockchain_address") REFERENCES "public"."app_user" ("blockchain_address")
);

COMMENT ON COLUMN "public"."nft_info"."nft_id" IS 'NFT ID';
COMMENT ON COLUMN "public"."nft_info"."image_id" IS '关联的图片ID';
COMMENT ON COLUMN "public"."nft_info"."minio_url" IS '图片的MinIO URL';
COMMENT ON COLUMN "public"."nft_info"."blockchain_address" IS '所有者Address';
COMMENT ON COLUMN "public"."nft_info"."token_id" IS '区块链上的Token ID';
COMMENT ON COLUMN "public"."nft_info"."contract_address" IS '智能合约地址';
COMMENT ON COLUMN "public"."nft_info"."price" IS '售价';
COMMENT ON COLUMN "public"."nft_info"."description" IS '描述';
COMMENT ON COLUMN "public"."nft_info"."is_for_sale" IS '是否在售';
COMMENT ON COLUMN "public"."nft_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."nft_info"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."nft_info"."is_delete" IS '是否删除';

-- ----------------------------
-- Table structure for nft_transaction
-- ----------------------------
DROP TABLE IF EXISTS "public"."nft_transaction";
CREATE TABLE "public"."nft_transaction" (
    "transaction_id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "nft_id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "from_user_id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "to_user_id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "price" decimal(20,8) NOT NULL,
    "transaction_hash" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP,
    "update_time" timestamp(6),
    "is_delete" bool NOT NULL DEFAULT false,
    CONSTRAINT "nft_transaction_pkey" PRIMARY KEY ("transaction_id"),
    CONSTRAINT "nft_transaction_nft_id_fkey" FOREIGN KEY ("nft_id") REFERENCES "public"."nft_info" ("nft_id"),
    CONSTRAINT "nft_transaction_from_user_id_fkey" FOREIGN KEY ("from_user_id") REFERENCES "public"."app_user" ("user_id"),
    CONSTRAINT "nft_transaction_to_user_id_fkey" FOREIGN KEY ("to_user_id") REFERENCES "public"."app_user" ("user_id")
);

COMMENT ON COLUMN "public"."nft_transaction"."transaction_id" IS '交易ID';
COMMENT ON COLUMN "public"."nft_transaction"."nft_id" IS 'NFT ID';
COMMENT ON COLUMN "public"."nft_transaction"."from_user_id" IS '卖方ID';
COMMENT ON COLUMN "public"."nft_transaction"."to_user_id" IS '买方ID';
COMMENT ON COLUMN "public"."nft_transaction"."price" IS '交易价格';
COMMENT ON COLUMN "public"."nft_transaction"."transaction_hash" IS '区块链交易哈希';
COMMENT ON COLUMN "public"."nft_transaction"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."nft_transaction"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."nft_transaction"."is_delete" IS '是否删除';

-- ----------------------------
-- Indexes for nft_info
-- ----------------------------
CREATE INDEX "nft_info_owner_id_index" ON "public"."nft_info" USING btree (
    "blockchain_address" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "nft_info_is_for_sale_index" ON "public"."nft_info" USING btree (
    "is_for_sale" ASC NULLS LAST
);
CREATE INDEX "nft_info_is_delete_index" ON "public"."nft_info" USING btree (
    "is_delete" ASC NULLS LAST
);

-- ----------------------------
-- Indexes for nft_transaction
-- ----------------------------
CREATE INDEX "nft_transaction_nft_id_index" ON "public"."nft_transaction" USING btree (
    "nft_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "nft_transaction_from_user_id_index" ON "public"."nft_transaction" USING btree (
    "from_user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "nft_transaction_to_user_id_index" ON "public"."nft_transaction" USING btree (
    "to_user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "nft_transaction_is_delete_index" ON "public"."nft_transaction" USING btree (
    "is_delete" ASC NULLS LAST
);