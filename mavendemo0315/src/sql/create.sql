/*
 Target Database: blockchain_db
 Description: 专为 FISCO BCOS/Web3 优化的区块链业务数据库
*/

-- ----------------------------
-- 1. 角色权限表 (App Role)
-- ----------------------------
DROP TABLE IF EXISTS "public"."app_role";
CREATE TABLE "public"."app_role" (
                                     "role_id" SERIAL PRIMARY KEY,
                                     "role_name" VARCHAR(50) NOT NULL UNIQUE,
                                     "description" VARCHAR(255)
);
COMMENT ON TABLE "public"."app_role" IS '应用角色权限表';

INSERT INTO app_role (role_name, description) VALUES
                                                  ('admin', '系统管理员'),
                                                  ('user', '普通用户');

-- ----------------------------
-- 2. 用户表 (App User) - 增加 Web3 登录支持
-- ----------------------------
DROP TABLE IF EXISTS "public"."app_user";
CREATE TABLE "public"."app_user" (
                                     "user_id" VARCHAR(100) NOT NULL,
                                     "user_name" VARCHAR(50) NOT NULL UNIQUE,
                                     "password_hash" VARCHAR(255) NOT NULL,
                                     "user_email" VARCHAR(100) NOT NULL UNIQUE,

    -- 区块链身份核心字段
                                     "blockchain_address" VARCHAR(255) UNIQUE, -- 用户的钱包地址 (0x...)
                                     "nonce" VARCHAR(100),                     -- 随机数，用于由 WeBASE/MetaMask 签名的登录验证
                                     "encrypted_private_key" VARCHAR(1000),    -- 托管钱包模式下，加密存储的用户私钥

                                     "role_id" INT NOT NULL,
                                     "avatar_url" VARCHAR(255),                -- !!! 新增: 头像
                                     "bio" VARCHAR(500),                       -- !!! 新增: 简介

                                     "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     "update_time" TIMESTAMP,
                                     "is_delete" BOOLEAN NOT NULL DEFAULT FALSE,
                                     PRIMARY KEY ("user_id")
);
COMMENT ON TABLE "public"."app_user" IS '应用用户表';
COMMENT ON COLUMN "public"."app_user"."nonce" IS 'Web3登录签名随机数，防止重放攻击';
COMMENT ON COLUMN "public"."app_user"."blockchain_address" IS 'FISCO BCOS 外部账户地址';

-- ----------------------------
-- 3. NFT 合集/系列表 (NFT Collection)
-- ----------------------------
-- 用于管理不同的智能合约，例如 "创世系列"、"活动系列"
DROP TABLE IF EXISTS "public"."nft_collection";
CREATE TABLE "public"."nft_collection" (
                                           "collection_id" SERIAL PRIMARY KEY,
                                           "name" VARCHAR(100) NOT NULL,          -- 系列名称
                                           "symbol" VARCHAR(20),                  -- 代币符号
                                           "contract_address" VARCHAR(255) NOT NULL UNIQUE, -- 部署在 FISCO 上的合约地址
                                           "cover_image" VARCHAR(255),            -- 系列封面图
                                           "description" TEXT,
                                           "creator_user_id" VARCHAR(100),        -- 创建者ID
                                           "create_time" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE "public"."nft_collection" IS 'NFT合约系列表';

-- ----------------------------
-- 4. NFT 信息表 (NFT Info)
-- ----------------------------
DROP TABLE IF EXISTS "public"."nft_info";
CREATE TABLE "public"."nft_info" (
                                     "nft_id" varchar(255) NOT NULL,

    -- 关联信息
                                     "collection_id" INT,                   -- 归属哪个系列
                                     "owner_address" varchar(255) NOT NULL, -- 当前持有者地址 (blockchain_address)
                                     "creator_address" varchar(255),        -- 初始铸造者地址

    -- 链上核心数据
                                     "token_id" varchar(255) NOT NULL,      -- 链上 Token ID
                                     "token_uri" varchar(500),              -- 链上 Metadata URI (IPFS/MinIO URL)
                                     "contract_address" varchar(255) NOT NULL, -- 冗余字段，方便查询

    -- 业务数据

                                     "image_id" varchar(100) NOT NULL,
                                     "image_url" varchar(255) NOT NULL,     -- 图片实际访问地址 (MinIO)
                                     "file_hash" varchar(64) NOT NULL,      -- 文件SHA-256哈希
                                     "price" decimal(20,8),
                                     "name" varchar(100),                   -- NFT名称
                                     "description" TEXT,
                                     "is_for_sale" bool NOT NULL DEFAULT true,

                                     "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                     "update_time" timestamp,
                                     "is_delete" bool NOT NULL DEFAULT false,

                                     CONSTRAINT "nft_info_pkey" PRIMARY KEY ("nft_id"),
                                     CONSTRAINT "fk_nft_collection" FOREIGN KEY ("collection_id") REFERENCES "public"."nft_collection" ("collection_id")
);
COMMENT ON TABLE "public"."nft_info" IS 'NFT资产详情表';
CREATE INDEX idx_nft_image_id ON nft_info(image_id);
CREATE INDEX uidx_nft_file_hash ON nft_info(file_hash);

-- ----------------------------
-- 5. NFT 交易记录表 (NFT Transaction)
-- ----------------------------
DROP TABLE IF EXISTS "public"."nft_transaction";
CREATE TABLE "public"."nft_transaction" (
                                            "transaction_id" varchar(100) NOT NULL,
                                            "transaction_hash" varchar(100) NOT NULL, -- 链上交易哈希

                                            "nft_id" varchar(100) NOT NULL,
                                            "from_address" varchar(255) NOT NULL,     -- 发送方钱包地址
                                            "to_address" varchar(255) NOT NULL,       -- 接收方钱包地址

                                            "price" decimal(20,8),

    -- 链上状态监控
                                            "status" INT2 DEFAULT 0 NOT NULL,         -- 0-打包中, 1-成功, 2-失败
                                            "type" VARCHAR(20),                       -- 交易类型: MINT, BUY, TRANSFER
                                            "block_number" INT8,                      -- 区块高度
                                            "group_id" INT4 DEFAULT 1,                -- FISCO BCOS 群组ID (默认1)

                                            "create_time" timestamp DEFAULT CURRENT_TIMESTAMP,
                                            "update_time" timestamp,
                                            CONSTRAINT "nft_transaction_pkey" PRIMARY KEY ("transaction_id")
);
COMMENT ON COLUMN "public"."nft_transaction"."status" IS '交易状态: 0-Pending, 1-Success, 2-Fail';
COMMENT ON COLUMN "public"."nft_transaction"."group_id" IS 'FISCO BCOS Group ID';

-- ----------------------------
-- 索引优化
-- ----------------------------
CREATE INDEX "idx_nft_owner" ON "public"."nft_info" ("owner_address");
CREATE INDEX "idx_nft_contract_token" ON "public"."nft_info" ("contract_address", "token_id");
CREATE INDEX "idx_tx_hash" ON "public"."nft_transaction" ("transaction_hash");
CREATE INDEX "idx_tx_status" ON "public"."nft_transaction" ("status");
CREATE INDEX "idx_user_address" ON "public"."app_user" ("blockchain_address");