# 文档与脚本概览

> 维护要求：新增、删除、重命名或修改任何 `docs`、`scripts`、`db/migration`、`game-config` 下的文档、SQL、脚本、配置文件时，必须同步更新本文档。

## 快速入口

| 想做什么 | 优先查看 |
| --- | --- |
| 了解项目当前做到哪一步 | [MVP_OUTLINE.md](MVP_OUTLINE.md) |
| 了解每个业务模块做什么 | [BUSINESS_MODULES.md](BUSINESS_MODULES.md) |
| 按接口做 Apifox/Postman 测试 | [API.md](API.md) |
| 查数据库怎么初始化 | [DATABASE.md](DATABASE.md) |
| 查每张表和字段含义 | [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) |
| 查开发规范和约束 | [DEVELOPMENT_CONVENTIONS.md](DEVELOPMENT_CONVENTIONS.md) |
| 查每次任务做了什么 | [../logs/CHANGELOG.md](../logs/CHANGELOG.md) |
| 本地用 JDK 21 构建 | [../scripts/dev-mvn.ps1](../scripts/dev-mvn.ps1) |
| 重建本地测试库 | [../scripts/01_full_reset_schema.sql](../scripts/01_full_reset_schema.sql) |

## docs 目录

| 文件 | 职能 | 使用场景 | 何时必须更新 |
| --- | --- | --- | --- |
| [DOCUMENT_INDEX.md](DOCUMENT_INDEX.md) | 文档、SQL、脚本、配置总索引 | 不知道该看哪个文件时，从这里进入 | 任意文档、SQL、脚本、配置新增/删除/重命名/职责变化 |
| [BUSINESS_MODULES.md](BUSINESS_MODULES.md) | 业务模块职责和 Controller 方法使用场景 | 理解 `modules` 下每个业务模块、接口何时调用、会影响哪些数据 | 新增/删除/重命名模块或 Controller 方法，调整业务场景 |
| [MVP_OUTLINE.md](MVP_OUTLINE.md) | MVP 总览、项目结构、模块职责、功能进度、测试入口 | 查看整体进展、判断下一步做什么、给接口测试找入口 | 功能状态变化、模块职责变化、目录结构变化、测试顺序变化 |
| [API.md](API.md) | 接口说明和 Apifox/Postman 测试顺序 | 手工测试接口、理解请求头和请求体 | 新增/修改/删除接口、DTO、鉴权方式、测试顺序 |
| [DATABASE.md](DATABASE.md) | 数据库初始化方式和迁移规则 | 首次建库、执行 Flyway、理解 SQL 文件职责 | 数据库初始化流程、迁移策略、SQL 文件变化 |
| [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) | 数据库表结构字典 | 查看表作用、字段作用、字段使用场景 | 新增/修改/删除表或字段、调整字段语义 |
| [DEVELOPMENT_CONVENTIONS.md](DEVELOPMENT_CONVENTIONS.md) | 开发约束和编码规范 | 开发前确认分层、接口、数据库、日志规则 | 规范变化、目录职责变化、流程约束变化 |

## scripts 目录

| 文件 | 职能 | 使用场景 | 何时必须更新本文档 |
| --- | --- | --- | --- |
| [../scripts/dev-mvn.ps1](../scripts/dev-mvn.ps1) | 本地 Maven 开发脚本，强制使用 JDK 21 和工作区 Maven 仓库 | 编译、打包、运行 Maven 命令 | JDK 路径、Maven 路径、settings 路径、构建方式变化 |
| [../scripts/00_create_database.sql](../scripts/00_create_database.sql) | 首次创建数据库和数据库用户 | 新服务器或新 MySQL 实例首次准备库 | 数据库名、用户、权限策略变化 |
| [../scripts/01_full_reset_schema.sql](../scripts/01_full_reset_schema.sql) | 完整覆盖初始化 SQL，包含删表、建表、注释、种子数据 | 本地或测试环境需要重建干净表结构 | 任意表、字段、索引、注释、种子数据变化 |

## Flyway 迁移目录

目录：`src/main/resources/db/migration`

| 文件 | 职能 | 使用场景 | 何时必须更新本文档 |
| --- | --- | --- | --- |
| `V1__init_mvp_schema.sql` | MVP 初始表结构 | 应用首次启动自动建表 | 旧迁移不再修改；如被废弃或替代需说明 |
| `V2__seed_mvp_config.sql` | MVP 配置版本种子数据 | 初始化 `game_config_version` | 配置版本种子策略变化 |
| `V3__add_schema_comments.sql` | 给已有表补充表注释和字段注释 | 已建库环境自动补注释 | 表/字段注释补丁变化 |

维护规则：

- 已提交的 `V*.sql` 原则上不修改。
- 新增结构变更时创建新的 `V{序号}__{说明}.sql`。
- 新增迁移脚本后必须同步更新本节、[DATABASE.md](DATABASE.md)、[DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) 和 [../logs/CHANGELOG.md](../logs/CHANGELOG.md)。

## game-config 目录

目录：`src/main/resources/game-config`

| 文件 | 职能 | 使用场景 | 何时必须更新本文档 |
| --- | --- | --- | --- |
| `crops.json` | MVP 作物配置，当前包含番茄、玉米、生菜 | 播种、成熟时间计算、作物配置下发 | 作物字段、作物ID、作物数值、配置加载方式变化 |

维护规则：

- 游戏数值和内容优先放配置，不写死在业务代码里。
- 配置字段变化时，需要同步更新接口说明、测试说明和相关 DTO/模型。
- 后续新增 `recipes.json`、`upgrades.json`、`random-events.json` 时，需要同步补充本文档。

## logs 目录

| 文件 | 职能 | 使用场景 | 何时必须更新本文档 |
| --- | --- | --- | --- |
| [../logs/CHANGELOG.md](../logs/CHANGELOG.md) | 开发日志，记录时间、版本、完成内容、修改文件、验证结果、回退建议 | 追溯每段任务、准备提交 Git、定位回退点 | 日志规则变化或文件迁移 |

## 同步更新检查清单

每次任务结束前检查：

| 变更类型 | 必须同步 |
| --- | --- |
| 新增/删除/重命名文档 | `DOCUMENT_INDEX.md`、`CHANGELOG.md` |
| 新增/修改接口 | `API.md`、`MVP_OUTLINE.md`、`CHANGELOG.md` |
| 新增/修改表或字段 | `DATABASE_SCHEMA.md`、`DATABASE.md`、`01_full_reset_schema.sql`、Flyway migration、`CHANGELOG.md` |
| 新增/修改 SQL 脚本 | `DOCUMENT_INDEX.md`、`DATABASE.md`、`CHANGELOG.md` |
| 新增/修改游戏配置 | `DOCUMENT_INDEX.md`、`MVP_OUTLINE.md`、`API.md` 如涉及接口、`CHANGELOG.md` |
| 新增/调整模块目录 | `MVP_OUTLINE.md`、`DEVELOPMENT_CONVENTIONS.md` 如涉及规则、`CHANGELOG.md` |
