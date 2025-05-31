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