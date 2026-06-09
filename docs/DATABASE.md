# 数据库说明

## 初始化顺序

1. 使用 MySQL root 执行 `scripts/00_create_database.sql`。
2. 启动应用，Flyway 自动执行 `src/main/resources/db/migration`。

## 核心表

| 表名 | 说明 |
| --- | --- |
| `player` | 玩家基础信息、金币、等级、收入统计 |
| `player_land` | 玩家土地状态、作物、成熟时间 |
| `player_inventory` | 仓库物品数量 |
| `player_upgrade` | 农场、摊位、建筑等升级状态 |
| `stall_session` | 摆摊局记录和结算摘要 |
| `economy_log` | 金币变动流水 |
| `ad_reward_record` | 广告奖励领取记录和幂等 |
| `achievement_progress` | 成就进度 |
| `game_config_version` | 游戏配置版本 |

详细表结构、字段含义和使用场景见 [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)。

## 迁移规则

- 已提交的 `V*.sql` 不再修改。
- 新增结构变更创建新的 `V{序号}__{说明}.sql`。
- 数据修复脚本放入 `scripts/manual`，执行前先备份。
- 任何表或字段变更必须同步更新 `docs/DATABASE_SCHEMA.md`。
