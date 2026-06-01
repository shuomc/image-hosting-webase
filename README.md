<div align="center">

# 基于区块链的原创图片保护与分享网站的设计与实现

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.x-blue.svg)](https://baomidou.com/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.37.x-yellow.svg)](https://sa-token.cc/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![MinIO](https://img.shields.io/badge/MinIO-Latest-orange.svg)](https://min.io/)
[![Gradle](https://img.shields.io/badge/Gradle-7.x/8.x-lightgray.svg)](https://gradle.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36.svg)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-316192.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Latest-2496ED.svg)](https://www.docker.com/)
[![Ubuntu](https://img.shields.io/badge/Ubuntu-22.04-E95420.svg)](https://ubuntu.com/)
[![Vue](https://img.shields.io/badge/Vue-3.x-42b883.svg)](https://vuejs.org/)
[![Blockchain](https://img.shields.io/badge/Blockchain-FISCO%20BCOS-blue.svg)](https://fisco-bcos-documentation.readthedocs.io/)

这是一个基于 Spring Boot 3 和 Vue 3 的全栈图像托管解决方案，集成了 FISCO BCOS 区块链技术实现 NFT 铸造与权属证明。

</div>

## ✨ 核心特性

- **🖼️ 图像管理**：支持高性能图片上传、多级分类、标签化管理及搜索。
- **🔗 区块链/NFT 铸造**：集成 FISCO BCOS 联盟链，支持将图片元数据上链，生成唯一的 NFT 凭证。
- **👤 权限与安全**：基于 **Sa-Token** 实现权限框架，支持多角色（管理员、普通用户）访问控制。
- **📊 社交互动**：提供点赞、收藏、评论及下载统计等丰富的社交功能。
- **🛠️ 管理后台**：完善的后台分析报表，实时监控系统状态、用户活动及区块链交易。
- **📦 分布式存储**：集成 **MinIO** 对象存储，支持海量图片数据存储与访问加速。

## 🛠️ 技术栈

### 后端 (主服务)
- **框架**: Java 14&17, Spring Boot 3.x
- **ORM**: MyBatis-Plus
- **安全**: Sa-Token
- **架构**: Gradle 多模块微内核架构

### 区块链中间层
- **框架**: Maven, Spring Boot
- **底层链**: FISCO BCOS
- **智能合约**: Solidity (WeBase 集成)

### 前端
- **框架**: Vue 3 (Composition API)
- **构建**: Vite
- **UI**: Element Plus + Tailwind CSS
- **状态管理**: Pinia

### 基础设施
- **数据库**: PostgreSQL
- **缓存**: Redis
- **对象存储**: MinIO
- **部署**: Docker & Docker Compose

## 📂 项目结构

```text
.
├── image-hosting/               # 主后端工程 (Gradle)
│   ├── image-hosting-starter    # 启动模块及配置汇聚
│   ├── image-hosting-framework  # 核心框架 (安全、Redis、数据源)
│   ├── image-hosting-images     # 图片与社交业务逻辑
│   ├── image-hosting-nft        # NFT 区块链集成逻辑
│   ├── image-hosting-system     # 用户身份与系统管理
│   └── web/vue/                 # Vue 3 前端工程
├── mavendemo0315/               # 区块链中间层 (Maven)
│   └── src/main/solidity        # 智能合约源代码
├── postgres-init/               # 数据库初始化 SQL 脚本
├── shell/                       # 运维脚本 (快速启停区块链服务)
└── docker-compose.yml           # Docker容器编排
```

## 🚀 快速开始

### 前置要求
- Docker & Docker Compose
- JDK 14 & 17+
- Node.js 18+
- Ubuntu 22.04

### ⛓️ 区块链环境准备
1.  **部署 WeBase & FISCO BCOS** (Ubuntu 22.04):
    *   参考 [WeBase 官方文档](https://webase-doc.readthedocs.io/en/latest/docs/WeBASE/install.html) 进行基础环境部署。
    *   **快捷脚本**: 部署完成后，可使用 `shell/restart_webase.sh` 和 `shell/stop_webase.sh` 快速启停服务。
    *   *注意：使用脚本前请根据实际安装路径修改脚本内部变量。*

2.  **部署智能合约**:
    *   **WeBase 后台**: 导航至 `合约管理` -> `合约 IDE` -> `新建合约`。
    *   **源代码**: 将 `mavendemo0315/src/solidity/ImageNFTv2.sol` 的内容粘贴至 IDE。
    *   **编译与发布**: 完成编译并部署，获取返回的合约地址。

3.  **后端集成配置**:
    *   **合约参数**: 修改 `mavendemo0315/src/main/java/com/sjy/imagechain/service/impl/NftServiceImpl.java` 中的相关常量：
        ```java
        private static final String CONTRACT_ADDRESS = "您的合约地址";
        private static final String CONTRACT_NAME = "您的合约名称";
        ```
    *   **ABI & Bin**: 将生成的 `abi` 和 `bytecodeBin` 文件分别放置于：
        *   `src/main/resources/abi/`
        *   `src/main/resources/bin/ecc/`
    *   **网络连接**: 修改 `mavendemo0315/src/main/resources/config-example.toml`，将 `peers` 列表中的 IP 替换为 Ubuntu 服务器的实际地址。

### ☁️ MinIO 对象存储配置
访问地址: [http://localhost:19090](http://localhost:19090)

1.  **桶 (Buckets) 初始化**:
    *   `image-origin`: 权限设置为 `private`。
    *   `image-thumbnail`: 权限设置为 `public`。
    *   `image-watermark`: 权限设置为 `public`。

2.  **手动管理**: 可通过 **Object Browser** 直接进行图片的上传与维护。

### 🐳 Docker 一键部署
1.  **进入目录**:
    ```bash
    cd image-hosting-webase
    ```
2.  **启动基础服务**:
    ```bash
    docker-compose up -d postgres redis minio
    ```
3.  **启动应用服务**:
    ```bash
    docker-compose up -d
    ```

### 💻 本地开发调试
*   **主后端**: 运行 `image-hosting-starter` 模块的 `ImageHostingApplication` (Port: `8080`)。
*   **区块链后端及服务**: 在 Ubuntu 环境中运行 `restart_webase.sh`，随后启动 `mavendemo0315` 下 `com.sjy.imagechain` 包中的启动类 (Port: `8081`)。
*   **前端**:
    ```bash
    cd image-hosting/web/vue
    npm install
    npm run dev
    ```

## 📊 数据库与存储
- **数据库**: 启动服务时会自动执行 `postgres-init/` 下的 SQL 脚本完成初始化。
- **MinIO 管理台**: `http://localhost:19090` (账号/密码: `minioadmin`/`minioadmin`)。
- **Redis 控制台**: 默认端口 `6379`。
