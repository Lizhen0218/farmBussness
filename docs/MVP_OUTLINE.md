# MVP 功能大纲、项目结构与测试视图

> 维护要求：每完成一个功能、调整一个模块职责、修改测试入口，都要同步更新本文档。

## 当前状态

后端已完成 Java 项目初始化、数据库初始化脚本、Swagger、玩家登录初始化、农场土地、作物配置、播种、收获入仓、仓库列表、仓库直接售出和金币流水第一版。

当前推荐测试入口：

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- 接口测试顺序说明：[API.md](API.md)
- 业务模块说明：[BUSINESS_MODULES.md](BUSINESS_MODULES.md)
- 数据库字典：[DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)
- 开发日志：[../logs/CHANGELOG.md](../logs/CHANGELOG.md)

## 项目目录结构

```text
farm-server
├── pom.xml                         Maven 项目配置
├── settings-workspace.xml          工作区 Maven settings，依赖下载到 D:\Farm\maven_repo
├── scripts
│   ├── dev-mvn.ps1                 强制使用 JDK 21 的 Maven 开发脚本
│   ├── 00_create_database.sql      首次创建数据库和用户的脚本
│   └── 01_full_reset_schema.sql    可覆盖重建表结构的完整初始化脚本
├── docs
│   ├── DEVELOPMENT_CONVENTIONS.md  开发约束
│   ├── API.md                      接口说明和测试顺序
│   ├── DATABASE.md                 数据库初始化和迁移规则
│   ├── DATABASE_SCHEMA.md          表和字段字典
│   └── MVP_OUTLINE.md              当前文件：整体大纲、结构和进度
├── logs
│   └── CHANGELOG.md                每段任务的时间、改动、验证和回退记录
└── src/main
    ├── java/com/farmgame/server
    │   ├── FarmServerApplication.java
    │   ├── common                  通用基础能力
    │   └── modules                 业务模块
    └── resources
        ├── application.yml         应用配置
        ├── db/migration            Flyway 数据库迁移脚本
        └── game-config             游戏 JSON 配置
```

## 后端模块职责

| 模块 | 路径 | 作用 | 当前状态 | 测试关注点 |
| --- | --- | --- | --- | --- |
| common/api | `common/api` | 统一返回、错误码 | 已完成第一版 | 所有接口是否统一返回 `ApiResponse` |
| common/config | `common/config` | OpenAPI、Jackson 等配置 | 已完成第一版 | Swagger 是否正常展示 |
| common/web | `common/web` | 全局异常处理 | 已完成第一版 | 参数错误是否返回统一错误 |
| user | `modules/user` | 登录、玩家基础信息、档案 | 已完成第一版 | 登录是否创建玩家，重复登录是否复用 |
| farm | `modules/farm` | 土地、播种、成熟、收获 | 已完成第一版 | 空地播种、未成熟拦截、成熟收获 |
| inventory | `modules/inventory` | 仓库物品 | 已完成列表、入仓和直接售出 | 收获后仓库数量是否增加，售出后金币是否增加 |
| gameconfig | `modules/gameconfig` | 作物等配置加载和下发 | 已完成作物配置 | `/api/config/crops` 是否返回三种作物 |
| economy | `modules/economy` | 金币和经济流水 | 已完成直接出售流水第一版 | 直接出售、摆摊结算、升级消耗 |
| stall | `modules/stall` | 摆摊局、订单、结算 | 接口占位 | 开局、结算、材料消耗 |
| leaderboard | `modules/leaderboard` | Redis 排行榜 | 接口占位 | 总榜、日榜 |
| ad | `modules/ad` | 广告奖励、防重复领取 | 接口占位 | 幂等领取、次数限制 |

## 第一阶段：后端基础

| 功能 | 状态 | 说明 |
| --- | --- | --- |
| Spring Boot 3 / Java 21 项目 | 已初始化 | `farm-server` |
| JDK 21 开发脚本 | 已初始化 | `scripts/dev-mvn.ps1` |
| Swagger/OpenAPI | 已初始化 | `/swagger-ui.html`、`/v3/api-docs` |
| 统一返回结构 | 已初始化 | `ApiResponse<T>` |
| 统一异常处理 | 已初始化 | `GlobalExceptionHandler` |
| MySQL 首次建库脚本 | 已初始化 | `scripts/00_create_database.sql` |
| 完整覆盖初始化 SQL | 已初始化 | `scripts/01_full_reset_schema.sql` |
| Flyway 迁移脚本 | 已初始化 | `src/main/resources/db/migration` |
| 数据库字典 | 已初始化 | `docs/DATABASE_SCHEMA.md` |

## 第二阶段：核心闭环

| 功能 | 状态 | 优先级 | 测试入口 |
| --- | --- | --- | --- |
| 用户登录与玩家初始化 | 已实现第一版 | P0 | `POST /api/user/login` |
| 6 块初始土地 | 已实现第一版 | P0 | 登录后 `GET /api/farm/state` |
| 3 种作物配置 | 已实现第一版 | P0 | `GET /api/config/crops` |
| 播种与成熟计算 | 已实现第一版 | P0 | `POST /api/farm/plant` |
| 收获入仓库 | 已实现第一版 | P0 | `POST /api/farm/harvest` |
| 仓库列表 | 已实现第一版 | P0 | `GET /api/inventory/list` |
| 仓库直接出售 | 已实现第一版 | P0 | `POST /api/inventory/sell` |
| 金币流水 | 已实现直接出售流水第一版 | P0 | `economy_log` |
| 摆摊开局 | 待实现 | P0 | `POST /api/stall/start` |
| 订单生成与结算校验 | 待实现 | P0 | `POST /api/stall/finish` |
| 摊位升级 | 待实现 | P1 | 待定 |
| 小偷事件 | 待实现 | P1 | 摆摊结算明细 |
| 财富大亨事件 | 待实现 | P1 | 摆摊结算明细 |
| 排行榜写入与查询 | 待实现 | P2 | `GET /api/leaderboard/income` |
| 广告奖励幂等领取 | 待实现 | P2 | `POST /api/ad/reward` |

## 当前接口测试顺序

1. `POST /api/user/login`：获取 `playerId`。
2. 后续请求添加请求头 `X-Player-Id: {playerId}`。
3. `GET /api/player/profile`：确认玩家档案和土地摘要。
4. `GET /api/farm/state`：确认 6 块土地。
5. `GET /api/config/crops`：确认三种作物配置。
6. `POST /api/farm/plant`：选空地播种。
7. 等待成熟后 `POST /api/farm/harvest`。
8. `GET /api/inventory/list`：确认作物进入仓库。
9. `POST /api/inventory/sell`：售出仓库作物，确认金币余额增加。

## 第三阶段：Cocos 接入准备

| 功能 | 状态 | 说明 |
| --- | --- | --- |
| OpenAPI 导出 | 已具备基础 | 后续随接口完善 |
| 配置 JSON 结构 | 已实现作物第一版 | 后续补菜谱、升级、随机事件 |
| 客户端协议文档 | 待实现 | 由接口 DTO 和 API 文档整理 |
| Cocos 工作区 | 已预留 | `D:\Farm\code\cocos-client` |
