# 开发约束

## 总原则

1. MVP 优先实现核心闭环：种植、收获、仓库、摆摊消耗、金币收益、升级。
2. 单体项目内保持模块边界清晰，不跨模块直接改表或绕过服务层。
3. 游戏数值、作物、菜谱、升级、事件和广告奖励必须配置化。
4. 后端不完全信任客户端结算，金币、材料、广告奖励、排行榜分数和随机事件奖励必须后端校验。
5. 每完成一个任务，在 `logs/CHANGELOG.md` 追加日期、版本、改动范围和回退提示。
6. 修改后端前先回看 `docs/BACKEND_ALIGNMENT.md`，避免偏离后端作为权威结算层的定位。
7. 修改 Cocos 前先回看 `D:\Farm\code\cocos-client\docs\CLIENT_STYLE_GUIDE.md` 和 `CLIENT_PROGRESS.md`，避免偏离 2.5D 农场与柜台式摆摊方向。
8. 跨端或大功能规划前先回看 `D:\Farm\docs\PROJECT_BRIEF.md`。

## 包结构

```text
com.farmgame.server
├── common              通用能力
│   ├── api             统一返回、错误码
│   ├── config          Spring/OpenAPI 配置
│   ├── exception       业务异常
│   └── web             Web 层通用处理
└── modules             业务模块
    ├── user            登录、玩家资料
    ├── farm            土地、播种、成熟、收获
    ├── inventory       仓库
    ├── stall           摆摊局、订单结算
    ├── economy         金币、流水、经济校验
    ├── leaderboard     排行榜
    ├── ad              广告奖励
    └── gameconfig      配置版本和配置下发
```

## 分层约束

- `controller`: 只处理 HTTP、参数校验、响应封装，不写核心业务。
- `service`: 编排业务规则、事务、幂等和后端校验。
- `repository/mapper`: 只处理持久化访问。
- `domain/entity`: 表达领域状态，不依赖 Web DTO。
- `dto`: 接口请求和响应对象。

## 实体类规范

- 实体类必须明确标注 `@TableName`。
- 主键字段必须明确标注 `@TableId`。
- 数据库字段必须明确标注 `@TableField`，不要只依赖驼峰转下划线的隐式映射。
- 实体类和字段必须同时具备 JavaDoc 注释和 `@Schema(description = "...")` 说明。
- 字段说明要与 `scripts/01_full_reset_schema.sql` 和 `docs/DATABASE_SCHEMA.md` 保持一致。

## DTO 规范

- 请求和响应 DTO 必须标注类级 `@Schema(description = "...")`。
- DTO 字段必须标注字段级 `@Schema(description = "...")`。
- 请求字段建议补充 `example`，方便 Swagger、Apifox 和 Postman 直接测试。
- DTO 字段说明要表达接口语义，不只重复字段名。

## 接口规范

- URL 使用 `/api/{module}/{action}` 或 REST 资源风格，保持稳定。
- 所有接口返回 `ApiResponse<T>`。
- 参数使用 Bean Validation 校验。
- 写接口要考虑幂等，尤其是广告奖励、摆摊结算、收获、出售。
- 新接口必须出现在 Swagger。

## 数据库规范

- 所有业务表使用 `BIGINT` 主键。
- 表字段统一包含 `created_at`、`updated_at`，需要软删的表包含 `deleted`。
- 金币和数量使用整数，避免浮点数。
- 金币变动必须写入 `economy_log`。
- 数据库结构变更必须新增 Flyway migration，不直接修改旧 migration。

## Redis 规范

- Key 使用 `farm:{env}:{module}:{business}:{id}`。
- 排行榜使用 Sorted Set。
- 广告和结算幂等可使用短期 Redis 锁，但最终以 MySQL 唯一键兜底。

## 日志与追溯

每个任务完成后追加：

```text
## yyyy-MM-dd HH:mm:ss +08:00 | v版本号 | 任务标题
- 完成内容：
- 修改文件：
- 验证结果：
- 回退建议：
```

## 本地私有配置

- 本地数据库和 Redis 真实连接信息写入 `config/application-local.yml`。
- `config/application-local.yml` 已加入 `.gitignore`，禁止提交真实密码、公网地址和私有连接配置。
- 首次本地启动前可执行 `scripts/create-local-config.ps1` 生成该文件。
- `application-dev.yml` 会通过 `spring.config.import` 自动读取本地私有配置，IDEA 直接启动时也会生效。

## 文档索引维护

- `docs/DOCUMENT_INDEX.md` 是文档、SQL、脚本和配置文件的总入口。
- 新增、删除、重命名或修改 `docs`、`scripts`、`src/main/resources/db/migration`、`src/main/resources/game-config` 下的文件时，必须同步更新 `docs/DOCUMENT_INDEX.md`。
- 修改 Cocos 客户端页面、场景、资源、交互或联调流程时，必须同步检查 `D:\Farm\code\cocos-client\docs\CLIENT_STYLE_GUIDE.md` 和 `CLIENT_PROGRESS.md`。
- 修改项目总体玩法闭环、目标优先级或端职责边界时，必须同步更新 `D:\Farm\docs\PROJECT_BRIEF.md`。
- 新增或修改表结构时，必须同步更新：
  - `docs/DATABASE_SCHEMA.md`
  - `docs/DATABASE.md`
  - `scripts/01_full_reset_schema.sql`
  - 对应 Flyway migration
  - `docs/DOCUMENT_INDEX.md`
  - `logs/CHANGELOG.md`
- 新增、删除、重命名业务模块或 Controller 方法时，必须同步更新：
  - `docs/BUSINESS_MODULES.md`
  - `docs/API.md`
  - `docs/MVP_OUTLINE.md`
  - `docs/DOCUMENT_INDEX.md`
  - `logs/CHANGELOG.md`
