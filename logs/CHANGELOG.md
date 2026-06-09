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
