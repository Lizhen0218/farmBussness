# 开发日志

## 2026-06-08 15:50:37 +08:00 | v0.1.0-init | Java 后端项目初始化

- 完成内容：
  - 创建 `farm-server` Spring Boot 3 / Java 21 后端项目骨架。
  - 接入 Swagger/OpenAPI、统一返回结构、统一异常处理。
  - 创建 MVP 主要接口占位：用户、玩家、农场、仓库、摆摊、排行榜、广告、配置。
  - 创建 MySQL 首次建库脚本和 Flyway 初始化迁移脚本。
  - 创建开发约束、MVP 大纲、接口说明、数据库说明。
  - 新增 `local-jdk17` Maven profile，便于当前本机在未安装 JDK 21 前先用 JDK 17 开发。
- 修改文件：
  - `pom.xml`
  - `src/main/java/com/farmgame/server/**`
  - `src/main/resources/application*.yml`
  - `src/main/resources/db/migration/*.sql`
  - `scripts/00_create_database.sql`
  - `docs/*.md`
  - `README.md`
- 验证结果：
  - 本机 Maven 可用。
  - `D:\code\Maven_repo\repository` 在当前工作区权限下不可写，已新增 `settings-workspace.xml` 指向 `D:\Farm\maven_repo`。
  - 本机默认 Java 为 1.8，当前未发现 JDK 21；默认 Java 21 构建需要配置 JDK 21。
  - 已准备使用本机 `C:\Program Files\Java\jdk-17.0.8` 执行 `local-jdk17` 构建验证。
  - 2026-06-08 16:07:56 +08:00：`mvn -s settings-workspace.xml -P local-jdk17 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - 当前 Windows 环境对 `target` 目录清理和 Spring Boot repackage 重命名存在拒绝访问，源码编译与普通 jar 打包已验证通过。
- 回退建议：
  - 初始化阶段可整体回退 `code/farm-server` 目录，或在 Git 初始化后使用版本回退。

## 2026-06-08 16:35:06 +08:00 | v0.1.1-dev-env-login | JDK 21 与玩家初始化

- 完成内容：
  - 通过 winget 安装 Eclipse Temurin JDK 21：`C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`。
  - 新增 `scripts/dev-mvn.ps1`，强制本项目使用 JDK 21、`settings-workspace.xml` 和 `D:\Farm\maven_repo`。
  - 实现登录第一版：开发期将 `code` 作为 openId 来源，首次登录自动创建玩家。
  - 实现新玩家默认初始化 6 块空土地。
  - 实现玩家档案查询和农场状态查询。
  - 开发期约定除登录外接口使用 `X-Player-Id` 请求头。
- 修改文件：
  - `scripts/dev-mvn.ps1`
  - `src/main/java/com/farmgame/server/FarmServerApplication.java`
  - `src/main/java/com/farmgame/server/common/config/JacksonConfig.java`
  - `src/main/java/com/farmgame/server/common/web/GlobalExceptionHandler.java`
  - `src/main/java/com/farmgame/server/modules/user/**`
  - `src/main/java/com/farmgame/server/modules/farm/**`
  - `docs/API.md`
  - `docs/MVP_OUTLINE.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - JDK 21 编译通过，编译源文件 26 个。
  - 机器级 Path 仍有 Oracle Java 8 的 `javapath` 优先级；项目内构建脚本已确保开发使用 JDK 21。
- 回退建议：
  - 业务代码可按 Git 回退本次 Java 文件改动。
  - JDK 21 如需卸载，可使用 Windows 应用管理或 `winget uninstall EclipseAdoptium.Temurin.21.JDK`。

## 2026-06-08 16:41:55 +08:00 | v0.1.2-farm-crop-inventory | 作物播种与收获入仓

- 完成内容：
  - 新增 `game-config/crops.json`，配置 MVP 三种作物：番茄、玉米、生菜。
  - 新增配置加载服务 `GameConfigService`，支持查询作物配置。
  - 新增仓库实体、Mapper、Service 和仓库列表接口。
  - 实现播种接口：校验土地归属、解锁状态、空地状态，按作物配置计算成熟时间。
  - 实现收获接口：校验成熟状态，收获后土地清空，作物进入仓库。
  - 调整 MyBatis Mapper 扫描范围，递归扫描 `com.farmgame.server.modules`。
- 修改文件：
  - `src/main/resources/game-config/crops.json`
  - `src/main/java/com/farmgame/server/FarmServerApplication.java`
  - `src/main/java/com/farmgame/server/modules/gameconfig/**`
  - `src/main/java/com/farmgame/server/modules/farm/**`
  - `src/main/java/com/farmgame/server/modules/inventory/**`
  - `docs/API.md`
  - `docs/MVP_OUTLINE.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - JDK 21 编译通过，编译源文件 36 个。
- 回退建议：
  - 如需回退该功能，回退本段新增的 config、farm、inventory、gameconfig 相关文件，并恢复文档状态。

## 2026-06-08 17:34:09 +08:00 | v0.1.3-schema-doc-entity-comments | 数据库注释与工程说明补强

- 完成内容：
  - 新增完整覆盖初始化 SQL：`scripts/01_full_reset_schema.sql`，包含建库、删表、建表、表注释、字段注释和配置版本种子数据。
  - 新增 Flyway 迁移脚本：`V3__add_schema_comments.sql`，用于给已存在表补充表说明和字段注释。
  - 新增数据库字典文档：`docs/DATABASE_SCHEMA.md`，说明各表作用、字段作用和使用场景。
  - 更新 `docs/DATABASE.md`，明确表字段变更必须同步更新数据库字典。
  - 为当前已使用实体 `Player`、`PlayerLand`、`PlayerInventory` 补充 `@TableId`、`@TableField` 和字段 JavaDoc。
  - 为核心 Service 方法补充方法 JavaDoc，说明调用场景、校验规则和副作用。
  - 重写 `docs/MVP_OUTLINE.md`，补充项目结构、模块职责、当前进度和接口测试顺序。
- 修改文件：
  - `scripts/01_full_reset_schema.sql`
  - `src/main/resources/db/migration/V3__add_schema_comments.sql`
  - `docs/DATABASE_SCHEMA.md`
  - `docs/DATABASE.md`
  - `docs/MVP_OUTLINE.md`
  - `src/main/java/com/farmgame/server/modules/user/entity/Player.java`
  - `src/main/java/com/farmgame/server/modules/farm/entity/PlayerLand.java`
  - `src/main/java/com/farmgame/server/modules/inventory/entity/PlayerInventory.java`
  - `src/main/java/com/farmgame/server/modules/user/service/PlayerService.java`
  - `src/main/java/com/farmgame/server/modules/farm/service/FarmService.java`
  - `src/main/java/com/farmgame/server/modules/inventory/service/InventoryService.java`
  - `src/main/java/com/farmgame/server/modules/gameconfig/service/GameConfigService.java`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - JDK 21 编译通过，编译源文件 36 个。
- 回退建议：
  - 如需回退数据库注释补强，删除 `V3__add_schema_comments.sql` 和 `scripts/01_full_reset_schema.sql`，并恢复相关文档。
  - 如需回退实体注解和注释，恢复三个实体类和四个 Service 文件。

## 2026-06-08 17:42:46 +08:00 | v0.1.4-document-index | 文档与脚本概览

- 完成内容：
  - 新增 `docs/DOCUMENT_INDEX.md`，作为文档、SQL、脚本和配置文件的总入口。
  - 在概览中标注每个 md/sql/脚本/配置文件的职能、使用场景和何时必须同步更新。
  - 更新 `docs/DEVELOPMENT_CONVENTIONS.md`，要求配置、SQL、文档相关变更必须同步维护 `DOCUMENT_INDEX.md`。
- 修改文件：
  - `docs/DOCUMENT_INDEX.md`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - 文档文件已创建并写入索引规则。
  - 本次未修改 Java 代码，未重新执行 Maven 编译。
- 回退建议：
  - 删除 `docs/DOCUMENT_INDEX.md`，并恢复 `docs/DEVELOPMENT_CONVENTIONS.md` 与 `logs/CHANGELOG.md` 的本段变更。

## 2026-06-08 17:56:48 +08:00 | v0.1.5-entity-schema-comments | 实体字段结构化注释

- 完成内容：
  - 为 `Player`、`PlayerLand`、`PlayerInventory` 实体类补充 `@Schema(description = "...")`。
  - 保留原有 JavaDoc 字段注释，同时让 Swagger/OpenAPI 模型也能读取字段说明。
  - 更新 `DEVELOPMENT_CONVENTIONS.md`，明确实体字段必须具备 `@TableField`、JavaDoc 和 `@Schema`。
- 修改文件：
  - `src/main/java/com/farmgame/server/modules/user/entity/Player.java`
  - `src/main/java/com/farmgame/server/modules/farm/entity/PlayerLand.java`
  - `src/main/java/com/farmgame/server/modules/inventory/entity/PlayerInventory.java`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - JDK 21 编译通过，编译源文件 36 个。
- 回退建议：
  - 如需回退，移除三个实体类上的 `@Schema` 注解和开发约束中的实体类规范补充。

## 2026-06-08 18:04:56 +08:00 | v0.1.6-dto-schema-comments | DTO 属性用途说明

- 完成内容：
  - 为当前所有 DTO 类补充类级 `@Schema(description = "...")`。
  - 为当前所有 DTO 属性补充字段级 `@Schema(description = "...")`，请求字段增加示例值。
  - 覆盖 DTO：登录、玩家档案、农场状态、播种、收获、仓库物品。
  - 更新 `DEVELOPMENT_CONVENTIONS.md`，新增 DTO 注释规范。
- 修改文件：
  - `src/main/java/com/farmgame/server/modules/user/dto/LoginRequest.java`
  - `src/main/java/com/farmgame/server/modules/user/dto/LoginResponse.java`
  - `src/main/java/com/farmgame/server/modules/user/dto/PlayerProfileResponse.java`
  - `src/main/java/com/farmgame/server/modules/farm/dto/FarmStateResponse.java`
  - `src/main/java/com/farmgame/server/modules/farm/dto/PlantRequest.java`
  - `src/main/java/com/farmgame/server/modules/farm/dto/PlantResponse.java`
  - `src/main/java/com/farmgame/server/modules/farm/dto/HarvestRequest.java`
  - `src/main/java/com/farmgame/server/modules/farm/dto/HarvestResponse.java`
  - `src/main/java/com/farmgame/server/modules/inventory/dto/InventoryItemResponse.java`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - JDK 21 编译通过，编译源文件 36 个。
- 回退建议：
  - 如需回退，移除 DTO 类和字段上的 `@Schema` 注解，并恢复开发约束中的 DTO 规范补充。

## 2026-06-08 18:14:06 +08:00 | v0.1.7-business-modules-doc | 业务模块说明文档

- 完成内容：
  - 新增 `docs/BUSINESS_MODULES.md`，说明 `modules` 下每个业务模块的业务作用、当前状态、关联数据和测试建议。
  - 为当前各 Controller 方法补充业务使用场景、前置条件和结果影响说明。
  - 更新 `DOCUMENT_INDEX.md`，将业务模块说明加入文档总入口。
  - 更新 `MVP_OUTLINE.md`，增加业务模块说明入口。
  - 更新 `DEVELOPMENT_CONVENTIONS.md`，要求业务模块或 Controller 方法变更时同步维护业务模块文档。
- 修改文件：
  - `docs/BUSINESS_MODULES.md`
  - `docs/DOCUMENT_INDEX.md`
  - `docs/MVP_OUTLINE.md`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - 文档文件已创建并加入索引。
  - 本次仅修改文档，未执行 Maven 编译。
- 回退建议：
  - 删除 `docs/BUSINESS_MODULES.md`，并恢复三个文档入口文件和本日志段落。

## 2026-06-09 09:41:52 +08:00 | v0.1.8-github-init-safety | GitHub 初始化前凭据脱敏

- 完成内容：
  - 初始化本地 Git 仓库，准备推送到 GitHub 仓库 `Lizhen0218/farmBussness`。
  - 提交前扫描并移除源码中的服务器公网地址和数据库/Redis 明文密码默认值。
  - `application.yml` 改为默认连接本地 `127.0.0.1`，密码默认空值，通过环境变量注入真实配置。
  - `scripts/00_create_database.sql` 中数据库用户密码改为 `CHANGE_ME_STRONG_PASSWORD` 占位。
  - 新增 `.env.example`，说明本地/部署环境需要配置的环境变量。
- 修改文件：
  - `src/main/resources/application.yml`
  - `scripts/00_create_database.sql`
  - `.env.example`
  - `logs/CHANGELOG.md`
- 验证结果：
  - 使用 `rg` 扫描提交源文件，未发现已知服务器密码、公网地址或浏览器截图中的密码字符串。
  - `target/` 和 `.env` 已通过 `.gitignore` 忽略，不进入提交。
- 回退建议：
  - 如需恢复本地连接，可在本机环境变量或 `.env` 中配置真实数据库和 Redis 信息，不建议写回 Git 仓库。

## 2026-06-09 10:45:00 +08:00 | v0.1.9-cocos-client-prototype | Cocos 客户端展示原型

- 完成内容：
  - 在 `D:\Farm\code\cocos-client` 初始化 Cocos Creator 3.8.8 空 2D 工程骨架。
  - 新增 `FarmApiClient.ts`，封装登录、玩家档案、作物配置、农场状态、播种、收获、仓库、摆摊等当前后端接口调用。
  - 新增 `FarmPrototype.ts`，用 Cocos TypeScript 代码实现农场、仓库、摆摊、配置四个基础展示页和播种/收获交互。
  - 新增 `preview.html`，提供无需打开 Cocos 编辑器即可预览的 Web 展示版，方便先看业务闭环和接口联调效果。
  - 新增 `assets/resources/art/*.svg`，作为当前 MVP 农场地块、作物、摊位、仓库、金币等静态资源。
  - 新增客户端说明文档 `README.md` 与 `docs/CLIENT_PROTOTYPE.md`，记录页面、资源、接口映射和后续迭代建议。
  - 新增后端 `CorsConfig`，允许本地 Cocos Web 预览和静态预览页跨域访问 `/api/**`。
- 修改文件：
  - `D:\Farm\code\cocos-client\package.json`
  - `D:\Farm\code\cocos-client\README.md`
  - `D:\Farm\code\cocos-client\preview.html`
  - `D:\Farm\code\cocos-client\docs\CLIENT_PROTOTYPE.md`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmApiClient.ts`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmPrototype.ts`
  - `D:\Farm\code\cocos-client\assets\resources\art\*.svg`
  - `D:\Farm\code\cocos-client\assets\resources\config\api.json`
  - `D:\Farm\code\farm-server\src\main\java\com\farmgame\server\common\config\CorsConfig.java`
  - `D:\Farm\code\farm-server\logs\CHANGELOG.md`
- 验证结果：
  - 客户端当前为工程原型和静态预览页，Cocos Creator 首次打开项目后会自动生成 `.meta` 文件。
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功，JDK 21 编译通过。
  - 内置 Browser 插件本次连接失败，未完成页面截图验证；可直接打开 `preview.html` 进行静态预览。
- 回退建议：
  - 如需回退客户端原型，删除 `D:\Farm\code\cocos-client` 目录中本次新增的脚本、资源、文档和预览页。
  - 如需回退跨域配置，删除 `CorsConfig.java` 并移除本日志段落。

## 2026-06-09 12:40:00 +08:00 | v0.1.10-local-config-and-cocos-reload | 后端本地启动配置与 Cocos 重新加载

- 完成内容：
  - 排查后端启动失败原因：`dev` 环境默认使用 `farm_game` 用户且密码为空，Flyway 获取 MySQL 连接失败。
  - 修改 `application-dev.yml`，自动读取本机私有配置 `config/application-local.yml`。
  - 新增 `config/application-local.example.yml`，提供 MySQL/Redis 本地配置模板。
  - 新增 `scripts/create-local-config.ps1`，用于交互式生成本机私有连接配置。
  - 更新 `.gitignore`，忽略 `config/application-local.yml`，避免真实数据库和 Redis 密码进入仓库。
  - 更新 `DOCUMENT_INDEX.md` 和 `DEVELOPMENT_CONVENTIONS.md`，补充本地私有配置使用规则。
  - 修复 Cocos 客户端 `FarmPrototype.ts` 中 `??` 和 `?.` 导致的 Creator 编译兼容问题。
  - 清理 Cocos `temp`、`library` 缓存并重新打开 `D:\Farm\code\cocos-client` 工程。
- 修改文件：
  - `src/main/resources/application-dev.yml`
  - `.gitignore`
  - `config/application-local.example.yml`
  - `scripts/create-local-config.ps1`
  - `docs/DOCUMENT_INDEX.md`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmPrototype.ts`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - `FarmPrototype.ts` 已移除 `??` 和 `?.` 语法。
  - Cocos Creator 3.8.8 已重新启动并加载 `D:\Farm\code\cocos-client`。
- 回退建议：
  - 如需回退本地配置方案，移除 `application-dev.yml` 中的 `spring.config.import`，删除 `config/application-local.example.yml` 和 `scripts/create-local-config.ps1`，并恢复文档与 `.gitignore`。
  - 如需回退 Cocos 语法兼容修复，恢复 `FarmPrototype.ts` 本次变更。

## 2026-06-09 16:10:00 +08:00 | v0.1.11-cocos-farm-land-visible | 农场土地展示与播种联调修复

- 完成内容：
  - 排查 `/api/farm/state`，确认后端当前玩家已返回 6 块默认土地。
  - 修复前端展示页土地区域不明显的问题，将农场区改为 3x2 的 2.5D 俯视地块网格。
  - 修复浏览器处理 Java Long 超大 ID 的精度问题：后端 JSON 序列化 Long 为字符串，避免 `playerId`、`landId` 被浏览器四舍五入。
  - Cocos 客户端接口模型同步将 `playerId`、`landId`、`serverTime` 调整为字符串。
  - 前端点击土地改为通过 `landIndex` 查找当前内存中的土地对象，降低 DOM dataset 对超大 ID 的依赖。
- 修改文件：
  - `src/main/java/com/farmgame/server/common/config/JacksonConfig.java`
  - `D:\Farm\code\cocos-client\preview.html`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmApiClient.ts`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `Invoke-RestMethod` 验证当前玩家 `/api/farm/state` 返回 6 块土地。
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - 后端运行进程需要重启后，Long 字符串序列化才会在 8080 服务中生效。
- 回退建议：
  - 如需回退 Long 字符串序列化，移除 `JacksonConfig` 中的 `ToStringSerializer` 配置，并恢复前端 ID 类型。
  - 如需回退农场地块 UI，恢复 `preview.html` 的旧 `.farm-grid` 和 `renderFarm` 实现。

## 2026-06-09 16:22:00 +08:00 | v0.1.12-startup-datasource-diagnostics | 后端启动数据库连接异常排查

- 完成内容：
  - 排查后端重启失败日志，定位为 Flyway 获取 MySQL 连接时超时：`Communications link failure`。
  - 确认当前缺少本机私有配置 `config/application-local.yml`，应用没有明确使用本机数据库连接配置。
  - 确认本机 `127.0.0.1:3306` 端口可连通，系统级/用户级 `FARM_*` 非密码环境变量未发现。
  - 新增启动早期诊断输出，在 Spring 环境准备阶段打印脱敏后的 `datasource.url`、`datasource.username`、`redis.endpoint`。
  - 新增容器启动成功后的非敏感连接信息日志，方便后续区分本机/远端配置。
- 修改文件：
  - `src/main/java/com/farmgame/server/FarmServerApplication.java`
  - `src/main/java/com/farmgame/server/common/config/StartupDiagnostics.java`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功。
  - 下次启动即使数据库连接失败，也会先在控制台输出 `[startup-diagnostics] datasource.url=...` 便于确认实际连接目标。
- 回退建议：
  - 如需回退启动诊断，删除 `StartupDiagnostics.java`，并恢复 `FarmServerApplication.java` 中的普通 `SpringApplication.run(...)` 启动方式。

## 2026-06-09 16:35:00 +08:00 | v0.1.13-project-direction-guardrails | 项目大方向约束文档

- 完成内容：
  - 新增 Farm 全局总纲 `D:\Farm\docs\PROJECT_BRIEF.md`，沉淀项目定位、MVP 闭环、画风方向和开发前检查入口。
  - 新增后端方向约束 `docs/BACKEND_ALIGNMENT.md`，明确后端作为权威业务结算层的职责和接口、数据库、日志约束。
  - 新增 Cocos 画风与交互约束 `D:\Farm\code\cocos-client\docs\CLIENT_STYLE_GUIDE.md`，明确 2.5D 农场视角、柜台式摆摊界面、视觉资产和交互反馈要求。
  - 新增 Cocos 进度文档 `D:\Farm\code\cocos-client\docs\CLIENT_PROGRESS.md`，记录当前接入接口、测试顺序和待办方向。
  - 更新 `DOCUMENT_INDEX.md` 和 `DEVELOPMENT_CONVENTIONS.md`，要求每次修改后端或 Cocos 前先回看对应方向约束。
- 修改文件：
  - `D:\Farm\docs\PROJECT_BRIEF.md`
  - `docs/BACKEND_ALIGNMENT.md`
  - `D:\Farm\code\cocos-client\docs\CLIENT_STYLE_GUIDE.md`
  - `D:\Farm\code\cocos-client\docs\CLIENT_PROGRESS.md`
  - `docs/DOCUMENT_INDEX.md`
  - `docs/DEVELOPMENT_CONVENTIONS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - 文档已创建并加入后端文档索引。
  - 本次仅新增和更新 Markdown 文档，未执行 Maven 编译。
- 回退建议：
  - 删除新增的四个约束文档，并恢复 `DOCUMENT_INDEX.md`、`DEVELOPMENT_CONVENTIONS.md` 和本日志段落。

## 2026-06-09 17:35:00 +08:00 | v0.1.14-farm-field-countdown-inventory-sell | 农场地面、实时倒计时与仓库售出
- 完成内容：
  - 后端新增 `POST /api/inventory/sell` 实际售出逻辑，按作物配置售价扣减仓库库存并增加玩家金币。
  - 新增 `EconomyLog` 实体与 Mapper，售出时写入 `economy_log`，方便后续追踪金币流水。
  - Web 预览页将农场改成一整块 12 区域地面：前 6 块接入已解锁土地，其余区域显示未解锁。
  - Web 预览页和 Cocos 脚本增加成熟倒计时实时刷新，不再依赖手动点击刷新。
  - 仓库库存卡片新增 `售出` 操作，售出成功后刷新库存并以后端返回金币余额更新界面。
  - 使用 Dashboard 管理的 Cocos Creator 3.8.8 重新打开 `D:\Farm\code\cocos-client` 工程。
  - 更新 MVP 大纲、业务模块说明、接口文档和 Cocos 进度文档，保持功能状态一致。
- 修改文件：
  - `src/main/java/com/farmgame/server/modules/economy/entity/EconomyLog.java`
  - `src/main/java/com/farmgame/server/modules/economy/mapper/EconomyLogMapper.java`
  - `src/main/java/com/farmgame/server/modules/inventory/controller/InventoryController.java`
  - `src/main/java/com/farmgame/server/modules/inventory/service/InventoryService.java`
  - `src/main/java/com/farmgame/server/modules/inventory/dto/SellItemRequest.java`
  - `src/main/java/com/farmgame/server/modules/inventory/dto/SellItemResponse.java`
  - `docs/API.md`
  - `docs/BUSINESS_MODULES.md`
  - `docs/MVP_OUTLINE.md`
  - `D:\Farm\code\cocos-client\preview.html`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmApiClient.ts`
  - `D:\Farm\code\cocos-client\assets\scripts\FarmPrototype.ts`
  - `D:\Farm\code\cocos-client\docs\CLIENT_PROGRESS.md`
  - `logs/CHANGELOG.md`
- 验证结果：
  - `powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\dev-mvn.ps1 '-Dmaven.test.skip=true' '-Dspring-boot.repackage.skip=true' package` 构建成功，当前后端源码编译通过。
  - `http://localhost:8080/api/health` 返回 `UP`，说明本地 8080 服务可访问。
  - 本次未直接调用售出接口做数据变更测试，避免未经确认消耗当前仓库库存；后端需重启后新接口才会在运行服务中生效。
- 回退建议：
  - 如需回退售出功能，移除新增的两个 DTO、`EconomyLog` 与 Mapper，并恢复 `InventoryController`、`InventoryService` 中的占位售出实现。
  - 如需回退客户端展示，恢复 `preview.html`、`FarmApiClient.ts`、`FarmPrototype.ts` 中本次土地整块化、倒计时和售出按钮相关改动。
