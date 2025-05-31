
一个基于java的前后端分离图床实现
## 技术选型:
* 后端：Spring Boot, MyBatis-Plus, Sa-Token
* 数据库：PostgreSQL, Redis
* 前端：Vue, ElmentUI, Tailwind

```
image-hosting-webase
├─ docker-compose.yml
├─ image-hosting
│  ├─ gradle
│  │  └─ wrapper
│  │     ├─ gradle-wrapper.jar
│  │     └─ gradle-wrapper.properties
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ image-hosting-common
│  │  └─ src
│  │     └─ main
│  │        ├─ java
│  │        │  └─ moe
│  │        │     └─ imtop1
│  │        │        └─ imagehosting
│  │        │           └─ common
│  │        │              ├─ constant
│  │        │              │  ├─ Constant.java
│  │        │              │  └─ ErrorMsg.java
│  │        │              ├─ dto
│  │        │              │  └─ AjaxResult.java
│  │        │              ├─ enums
│  │        │              │  ├─ BooleanEnum.java
│  │        │              │  ├─ ResultCodeEnum.java
│  │        │              │  └─ StrategiesEnum.java
│  │        │              └─ utils
│  │        │                 ├─ FileUtil.java
│  │        │                 ├─ HtmlUtil.java
│  │        │                 ├─ JsonUtil.java
│  │        │                 ├─ StringUtil.java
│  │        │                 └─ TraceUtil.java
│  │        └─ resources
│  │           └─ templates
│  │              └─ email
│  │                 └─ auth
│  │                    └─ registration
│  │                       └─ captcha.ftl
│  ├─ image-hosting-framework
│  │  ├─ image-hosting-framework-core
│  │  │  └─ src
│  │  │     └─ main
│  │  │        ├─ java
│  │  │        │  └─ moe
│  │  │        │     └─ imtop1
│  │  │        │        └─ imagehosting
│  │  │        │           └─ framework
│  │  │        │              ├─ config
│  │  │        │              │  └─ AsyncConfig.java
│  │  │        │              ├─ exception
│  │  │        │              │  ├─ CustomException.java
│  │  │        │              │  ├─ GlobalExceptionHandler.java
│  │  │        │              │  ├─ ServiceException.java
│  │  │        │              │  └─ SystemException.java
│  │  │        │              └─ log
│  │  │        │                 └─ ModifyLogAspect.java
│  │  │        └─ resources
│  │  │           └─ META-INF
│  │  │              └─ spring
│  │  │                 └─ org.springframework.boot.autoconfigure.AutoConfiguration.imports
│  │  ├─ image-hosting-framework-datasource
│  │  │  └─ src
│  │  │     └─ main
│  │  │        ├─ java
│  │  │        │  └─ moe
│  │  │        │     └─ imtop1
│  │  │        │        └─ imagehosting
│  │  │        │           └─ framework
│  │  │        │              ├─ base
│  │  │        │              │  └─ BaseEntity.java
│  │  │        │              ├─ config
│  │  │        │              │  └─ MyBatisPlusConfig.java
│  │  │        │              └─ handler
│  │  │        │                 ├─ AutoFillDateHandler.java
│  │  │        │                 ├─ EncryptTypeHandler.java
│  │  │        │                 └─ JsonMapTypeHandler.java
│  │  │        └─ resources
│  │  │           └─ META-INF
│  │  │              └─ spring
│  │  │                 └─ org.springframework.boot.autoconfigure.AutoConfiguration.imports
│  │  ├─ image-hosting-framework-redis
│  │  │  └─ src
│  │  │     └─ main
│  │  │        ├─ java
│  │  │        │  └─ moe
│  │  │        │     └─ imtop1
│  │  │        │        └─ imagehosting
│  │  │        │           └─ framework
│  │  │        │              ├─ config
│  │  │        │              │  └─ RedisConfig.java
│  │  │        │              └─ utils
│  │  │        │                 └─ RedisCache.java
│  │  │        └─ resources
│  │  │           └─ META-INF
│  │  │              └─ spring
│  │  │                 └─ org.springframework.boot.autoconfigure.AutoConfiguration.imports
│  │  └─ image-hosting-framework-security
│  │     └─ src
│  │        └─ main
│  │           ├─ java
│  │           │  └─ moe
│  │           │     └─ imtop1
│  │           │        └─ imagehosting
│  │           │           └─ framework
│  │           │              ├─ AESKeyGenerator.java
│  │           │              ├─ config
│  │           │              │  └─ SaTokenConfig.java
│  │           │              ├─ domain
│  │           │              │  └─ LoginUser.java
│  │           │              ├─ KeyIVLengthChecker.java
│  │           │              └─ utils
│  │           │                 ├─ EncryptUtil.java
│  │           │                 └─ SecurityUtil.java
│  │           └─ resources
│  │              └─ META-INF
│  │                 └─ spring
│  │                    └─ org.springframework.boot.autoconfigure.AutoConfiguration.imports
│  ├─ image-hosting-images
│  │  └─ src
│  │     └─ main
│  │        ├─ java
│  │        │  └─ moe
│  │        │     └─ imtop1
│  │        │        └─ imagehosting
│  │        │           └─ images
│  │        │              ├─ config
│  │        │              │  └─ MinioConfig.java
│  │        │              ├─ controller
│  │        │              │  └─ ImageController.java
│  │        │              ├─ domain
│  │        │              │  ├─ dto
│  │        │              │  │  ├─ BatchUploadResult.java
│  │        │              │  │  └─ ImageStreamData.java
│  │        │              │  ├─ ImageData.java
│  │        │              │  ├─ Strategies.java
│  │        │              │  └─ vo
│  │        │              │     └─ ImageUrlData.java
│  │        │              ├─ mapper
│  │        │              │  ├─ ImageDataMapper.java
│  │        │              │  ├─ ImageMapper.java
│  │        │              │  └─ StrategiesMapper.java
│  │        │              └─ service
│  │        │                 ├─ ImageCacheService.java
│  │        │                 ├─ ImageService.java
│  │        │                 ├─ IMinioService.java
│  │        │                 └─ impl
│  │        │                    ├─ ImageCacheServiceImpl.java
│  │        │                    ├─ ImageServiceImpl.java
│  │        │                    └─ IMinioServiceImpl.java
│  │        └─ resources
│  │           └─ mapper
│  │              ├─ ImageDataMapper.xml
│  │              └─ StrategiesMapper.xml
│  ├─ image-hosting-starter
│  │  └─ src
│  │     ├─ main
│  │     │  ├─ java
│  │     │  │  └─ moe
│  │     │  │     └─ imtop1
│  │     │  │        └─ imagehosting
│  │     │  │           └─ starter
│  │     │  │              └─ Application.java
│  │     │  └─ resources
│  │     │     ├─ application-dev.yml
│  │     │     ├─ application-shuomc.yml
│  │     │     ├─ application.yml
│  │     │     └─ logback-spring.xml
│  │     └─ test
│  │        └─ java
│  │           └─ moe
│  │              └─ imtop1
│  │                 └─ imagehosting
│  │                    └─ starter
│  │                       └─ AppTest.java
│  ├─ image-hosting-system
│  │  └─ src
│  │     ├─ main
│  │     │  ├─ java
│  │     │  │  └─ moe
│  │     │  │     └─ imtop1
│  │     │  │        └─ imagehosting
│  │     │  │           └─ system
│  │     │  │              ├─ config
│  │     │  │              │  ├─ EmailTestConfig.java
│  │     │  │              │  └─ RestTemplateConfig.java
│  │     │  │              ├─ controller
│  │     │  │              │  ├─ EmailController.java
│  │     │  │              │  ├─ LoginController.java
│  │     │  │              │  ├─ ModifyController.java
│  │     │  │              │  ├─ NFTController.java
│  │     │  │              │  ├─ RegisterController.java
│  │     │  │              │  └─ UserController.java
│  │     │  │              ├─ domain
│  │     │  │              │  ├─ Config.java
│  │     │  │              │  ├─ dto
│  │     │  │              │  │  ├─ EmailCaptchaDTO.java
│  │     │  │              │  │  ├─ LoginDTO.java
│  │     │  │              │  │  └─ RegisterDTO.java
│  │     │  │              │  ├─ Permissions.java
│  │     │  │              │  ├─ RolePermissionRel.java
│  │     │  │              │  ├─ Roles.java
│  │     │  │              │  ├─ UserInfo.java
│  │     │  │              │  └─ vo
│  │     │  │              │     ├─ EmailCaptchaVO.java
│  │     │  │              │     ├─ LoginVO.java
│  │     │  │              │     └─ ValidateCodeVo.java
│  │     │  │              ├─ entity
│  │     │  │              │  ├─ ArtworkAsset.java
│  │     │  │              │  ├─ NFTInfo.java
│  │     │  │              │  └─ NFTTransaction.java
│  │     │  │              ├─ mapper
│  │     │  │              │  ├─ ArtworkAssetMapper.java
│  │     │  │              │  ├─ GlobalSettingsMapper.java
│  │     │  │              │  └─ UserInfoMapper.java
│  │     │  │              └─ service
│  │     │  │                 ├─ IEmailService.java
│  │     │  │                 ├─ ILoginService.java
│  │     │  │                 ├─ impl
│  │     │  │                 │  ├─ EmailServiceImpl.java
│  │     │  │                 │  ├─ LoginServiceImpl.java
│  │     │  │                 │  ├─ NFTServiceImpl.java
│  │     │  │                 │  ├─ RegisterServiceImpl.java
│  │     │  │                 │  ├─ UserInfoServiceImpl.java
│  │     │  │                 │  └─ ValidateCodeServiceImpl.java
│  │     │  │                 ├─ IRegisterService.java
│  │     │  │                 ├─ IUserInfoService.java
│  │     │  │                 ├─ IValidateCodeService.java
│  │     │  │                 └─ NFTService.java
│  │     │  └─ resources
│  │     │     ├─ application.yml
│  │     │     └─ mapper
│  │     │        ├─ GlobalSettingsMapper.xml
│  │     │        └─ UserInfoMapper.xml
│  │     └─ test
│  │        └─ java
│  │           └─ EmailServiceImplTest.java
│  ├─ logs
│  │  └─ image-hosting
│  │     ├─ 2025-02
│  │     │  ├─ debug.2025-02-25.0.log.gz
│  │     │  ├─ debug.2025-02-28.0.log.gz
│  │     │  └─ error.2025-02-25.0.log.gz
│  │     ├─ 2025-03
│  │     │  ├─ debug.2025-03-01.0.log.gz
│  │     │  ├─ debug.2025-03-02.0.log.gz
│  │     │  ├─ debug.2025-03-11.0.log.gz
│  │     │  └─ error.2025-03-22.0.log.gz
│  │     ├─ 2025-04
│  │     │  ├─ debug.2025-04-30.0.log.gz
│  │     │  └─ error.2025-04-30.0.log.gz
│  │     ├─ 2025-05
│  │     │  ├─ debug.2025-05-21.0.log.gz
│  │     │  ├─ debug.2025-05-23.0.log.gz
│  │     │  ├─ debug.2025-05-26.0.log.gz
│  │     │  └─ debug.2025-05-28.0.log.gz
│  │     ├─ debug.log
│  │     └─ error.log
│  ├─ README.md
│  ├─ sql
│  │  └─ create.sql
│  └─ web
│     └─ vue
│        ├─ .editorconfig
│        ├─ dockerfile
│        ├─ env.d.ts
│        ├─ eslint.config.ts
│        ├─ index.html
│        ├─ package-lock.json
│        ├─ package.json
│        ├─ postcss.config.js
│        ├─ public
│        │  └─ favicon.ico
│        ├─ README.md
│        ├─ src
│        │  ├─ api
│        │  │  ├─ auth
│        │  │  │  ├─ login.ts
│        │  │  │  ├─ password.ts
│        │  │  │  ├─ register.ts
│        │  │  │  └─ validate.ts
│        │  │  ├─ images
│        │  │  │  └─ index.ts
│        │  │  ├─ nft
│        │  │  │  └─ index.ts
│        │  │  └─ nft.ts
│        │  ├─ App.vue
│        │  ├─ assets
│        │  │  ├─ base.css
│        │  │  ├─ logo.png
│        │  │  ├─ logo.svg
│        │  │  └─ main.css
│        │  ├─ components
│        │  │  ├─ FindPasswordComponent.vue
│        │  │  ├─ icons
│        │  │  │  ├─ IconCommunity.vue
│        │  │  │  ├─ IconDocumentation.vue
│        │  │  │  ├─ IconEcosystem.vue
│        │  │  │  ├─ IconSupport.vue
│        │  │  │  └─ IconTooling.vue
│        │  │  ├─ LoginComponent.vue
│        │  │  ├─ RegisterLeftComponent.vue
│        │  │  ├─ RegisterRightComponent.vue
│        │  │  ├─ UploadFilesComponent.vue
│        │  │  ├─ WorkplaceLeftbarComponent.vue
│        │  │  └─ WorkplaceTopbarComponent.vue
│        │  ├─ config
│        │  │  └─ index.ts
│        │  ├─ config.ts
│        │  ├─ main.ts
│        │  ├─ router
│        │  │  └─ index.ts
│        │  ├─ stores
│        │  │  ├─ counter.ts
│        │  │  └─ user.ts
│        │  ├─ utils
│        │  │  └─ request.ts
│        │  └─ views
│        │     ├─ auth
│        │     │  ├─ FindPasswordView.vue
│        │     │  └─ LoginView.vue
│        │     ├─ HomeView.vue
│        │     ├─ nft
│        │     │  ├─ components
│        │     │  │  ├─ NFTDetail.vue
│        │     │  │  └─ NFTMint.vue
│        │     │  ├─ market
│        │     │  │  └─ index.vue
│        │     │  ├─ mint
│        │     │  │  └─ index.vue
│        │     │  ├─ my-nfts
│        │     │  │  └─ index.vue
│        │     │  ├─ MyNFTView.vue
│        │     │  ├─ NFTBalance.vue
│        │     │  ├─ NFTDetail.vue
│        │     │  ├─ NFTListView.vue
│        │     │  └─ NFTTransactions.vue
│        │     ├─ NotFoundView.vue
│        │     ├─ register
│        │     │  └─ RegisterView.vue
│        │     ├─ userui
│        │     │  ├─ components
│        │     │  │  └─ FooterComponent.vue
│        │     │  └─ Home.vue
│        │     └─ workplace
│        │        ├─ AboutView.vue
│        │        ├─ AccountSettingsView.vue
│        │        ├─ ImagesDetailView.vue
│        │        ├─ MyFilesView.vue
│        │        ├─ MyImagesView.vue
│        │        ├─ RecommendedView.vue
│        │        ├─ SettingsView.vue
│        │        ├─ UploadFileView.vue
│        │        ├─ UploadImageView.vue
│        │        └─ WorkplaceView.vue
│        ├─ tailwind.config.js
│        ├─ tsconfig.app.json
│        ├─ tsconfig.json
│        ├─ tsconfig.node.json
│        └─ vite.config.ts
├─ LICENSE
├─ mavendemo0315
│  ├─ dockerfile
│  ├─ logs
│  │  └─ image-chain.log
│  ├─ pom.xml
│  ├─ src
│  │  ├─ main
│  │  │  ├─ java
│  │  │  │  └─ com
│  │  │  │     └─ sjy
│  │  │  │        └─ imagechain
│  │  │  │           ├─ aspect
│  │  │  │           │  └─ LogAspect.java
│  │  │  │           ├─ common
│  │  │  │           │  ├─ BaseEntity.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  └─ AjaxResult.java
│  │  │  │           │  └─ enums
│  │  │  │           │     └─ ResultCodeEnum.java
│  │  │  │           ├─ config
│  │  │  │           │  ├─ BCOSConfig.java
│  │  │  │           │  ├─ RestTemplateConfig.java
│  │  │  │           │  └─ SecurityConfig.java
│  │  │  │           ├─ controller
│  │  │  │           │  ├─ NFTController.java
│  │  │  │           │  └─ UserController.java
│  │  │  │           ├─ domain
│  │  │  │           │  ├─ AppRole.java
│  │  │  │           │  ├─ AppUser.java
│  │  │  │           │  ├─ ArtworkAsset.java
│  │  │  │           │  ├─ config
│  │  │  │           │  │  └─ DatabaseObjectHandler.java
│  │  │  │           │  ├─ dto
│  │  │  │           │  │  ├─ UserRegistrationRequest.java
│  │  │  │           │  │  └─ UserRegistrationResponse.java
│  │  │  │           │  ├─ NFTInfo.java
│  │  │  │           │  └─ NFTTransaction.java
│  │  │  │           ├─ ImageChainStarter.java
│  │  │  │           ├─ mapper
│  │  │  │           │  ├─ AppRoleMapper.java
│  │  │  │           │  ├─ AppUserMapper.java
│  │  │  │           │  ├─ ArtworkAssetMapper.java
│  │  │  │           │  ├─ NFTInfoMapper.java
│  │  │  │           │  └─ NFTTransactionMapper.java
│  │  │  │           ├─ service
│  │  │  │           │  ├─ AppUserService.java
│  │  │  │           │  ├─ impl
│  │  │  │           │  │  ├─ AppUserServiceImpl.java
│  │  │  │           │  │  └─ NFTServiceImpl.java
│  │  │  │           │  └─ NFTService.java
│  │  │  │           └─ utils
│  │  │  │              ├─ StringUtil.java
│  │  │  │              └─ UserUtils.java
│  │  │  └─ resources
│  │  │     ├─ abi
│  │  │     │  ├─ Grade.abi
│  │  │     │  └─ ImageNFT.abi
│  │  │     ├─ application.yml
│  │  │     ├─ bin
│  │  │     ├─ conf
│  │  │     │  ├─ ca.crt
│  │  │     │  ├─ sdk.crt
│  │  │     │  └─ sdk.key
│  │  │     ├─ config-example.toml
│  │  │     ├─ mapper
│  │  │     │  └─ NFTTransactionMapper.xml
│  │  │     └─ static
│  │  │        ├─ index.html
│  │  │        └─ js
│  │  │           ├─ api
│  │  │           │  └─ auth
│  │  │           └─ stores
│  │  ├─ solidity
│  │  │  └─ ImageNFT.sol
│  │  ├─ sql
│  │  │  └─ create.sql
│  │  └─ test
│  │     └─ java
│  │        └─ learn
│  │           └─ mavendemo0315
│  │              ├─ Mavendemo0315ApplicationTests.java
│  │              └─ webase.java
│  └─ target
│     ├─ classes
│     │  ├─ abi
│     │  │  ├─ Grade.abi
│     │  │  └─ ImageNFT.abi
│     │  ├─ application.yml
│     │  ├─ com
│     │  │  └─ sjy
│     │  │     └─ imagechain
│     │  │        ├─ aspect
│     │  │        │  └─ LogAspect.class
│     │  │        ├─ common
│     │  │        │  ├─ BaseEntity.class
│     │  │        │  ├─ dto
│     │  │        │  │  └─ AjaxResult.class
│     │  │        │  └─ enums
│     │  │        │     └─ ResultCodeEnum.class
│     │  │        ├─ config
│     │  │        │  ├─ BCOSConfig.class
│     │  │        │  ├─ RestTemplateConfig.class
│     │  │        │  ├─ SecurityConfig$1.class
│     │  │        │  └─ SecurityConfig.class
│     │  │        ├─ controller
│     │  │        │  ├─ NFTController.class
│     │  │        │  └─ UserController.class
│     │  │        ├─ domain
│     │  │        │  ├─ AppRole.class
│     │  │        │  ├─ AppUser.class
│     │  │        │  ├─ ArtworkAsset.class
│     │  │        │  ├─ config
│     │  │        │  │  └─ DatabaseObjectHandler.class
│     │  │        │  ├─ dto
│     │  │        │  │  ├─ UserRegistrationRequest.class
│     │  │        │  │  └─ UserRegistrationResponse.class
│     │  │        │  ├─ NFTInfo.class
│     │  │        │  └─ NFTTransaction.class
│     │  │        ├─ ImageChainStarter.class
│     │  │        ├─ mapper
│     │  │        │  ├─ AppRoleMapper.class
│     │  │        │  ├─ AppUserMapper.class
│     │  │        │  ├─ ArtworkAssetMapper.class
│     │  │        │  ├─ NFTInfoMapper.class
│     │  │        │  └─ NFTTransactionMapper.class
│     │  │        ├─ service
│     │  │        │  ├─ AppUserService.class
│     │  │        │  ├─ impl
│     │  │        │  │  ├─ AppUserServiceImpl.class
│     │  │        │  │  └─ NFTServiceImpl.class
│     │  │        │  └─ NFTService.class
│     │  │        └─ utils
│     │  │           ├─ StringUtil.class
│     │  │           └─ UserUtils.class
│     │  ├─ conf
│     │  │  ├─ ca.crt
│     │  │  ├─ sdk.crt
│     │  │  └─ sdk.key
│     │  ├─ config-example.toml
│     │  ├─ mapper
│     │  │  └─ NFTTransactionMapper.xml
│     │  └─ static
│     │     └─ index.html
│     ├─ generated-sources
│     │  └─ annotations
│     ├─ generated-test-sources
│     │  └─ test-annotations
│     └─ test-classes
│        └─ learn
│           └─ mavendemo0315
│              ├─ Mavendemo0315ApplicationTests.class
│              └─ webase.class
├─ nginx
│  ├─ conf.d
│  │  └─ default.conf
│  └─ nginx.conf
└─ README.md

```