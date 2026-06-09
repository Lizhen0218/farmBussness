# 数据库表结构字典

> 维护要求：任何新增、修改、删除表或字段的任务，都必须同步更新本文档和对应 SQL 脚本。  
> 完整覆盖初始化脚本：`scripts/01_full_reset_schema.sql`  
> Flyway 迁移目录：`src/main/resources/db/migration`

## player

表说明：玩家基础信息表。

使用场景：登录、玩家档案、金币余额、等级经验、排行榜收入统计。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 玩家ID，雪花算法生成 | 全局玩家主键 |
| `open_id` | VARCHAR(128) | 微信 openId；开发期使用 `dev_code` | 登录唯一识别 |
| `nick_name` | VARCHAR(64) | 玩家昵称 | 个人资料、排行榜展示 |
| `avatar_url` | VARCHAR(512) | 玩家头像URL | 个人资料、排行榜展示 |
| `level` | INT | 玩家等级 | 解锁作物、菜品、建筑 |
| `exp` | BIGINT | 玩家经验值 | 成长系统 |
| `coins` | BIGINT | 当前金币余额 | 播种、出售、升级、摆摊结算 |
| `total_income` | BIGINT | 历史累计收入 | 总收入排行榜 |
| `daily_income` | BIGINT | 当日收入 | 每日收入排行榜 |
| `last_login_at` | DATETIME | 最近登录时间 | 登录奖励、活跃统计 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记：0未删除，1已删除 | 软删除 |

## player_land

表说明：玩家土地状态表。

使用场景：农场状态查询、播种、成熟判断、收获。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 土地ID，雪花算法生成 | 土地主键 |
| `player_id` | BIGINT | 玩家ID | 土地归属校验 |
| `land_index` | INT | 土地序号，从1开始 | 客户端展示排序 |
| `status` | VARCHAR(32) | 土地状态：EMPTY、GROWING | 播种、收获状态机 |
| `crop_id` | VARCHAR(64) | 当前种植作物ID | 作物配置关联 |
| `planted_at` | DATETIME | 播种时间 | 展示和成熟计算 |
| `mature_at` | DATETIME | 成熟时间 | 收获校验 |
| `unlocked` | TINYINT | 是否已解锁：0未解锁，1已解锁 | 土地扩建 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记 | 软删除 |

## player_inventory

表说明：玩家仓库物品表。

使用场景：收获入仓、仓库列表、直接出售、摆摊材料消耗。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 仓库记录ID | 仓库主键 |
| `player_id` | BIGINT | 玩家ID | 仓库归属 |
| `item_id` | VARCHAR(64) | 物品ID，例如 tomato、corn、lettuce | 配置关联 |
| `item_type` | VARCHAR(32) | 物品类型：CROP、MATERIAL、PROP | 分类展示 |
| `quantity` | BIGINT | 物品数量 | 出售和摆摊消耗校验 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记 | 软删除 |

## player_upgrade

表说明：玩家升级状态表。

使用场景：摊位升级、仓库升级、农场建筑升级、装饰轻属性等级。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 升级记录ID | 升级主键 |
| `player_id` | BIGINT | 玩家ID | 升级归属 |
| `upgrade_type` | VARCHAR(32) | 升级类型：FARM、STALL、WAREHOUSE、DECORATION | 升级分类 |
| `target_id` | VARCHAR(64) | 升级目标ID | 指向具体建筑或装饰 |
| `level` | INT | 当前等级 | 属性计算、外观阶段 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记 | 软删除 |

## stall_session

表说明：摆摊营业局记录表。

使用场景：摆摊开局、营业结算、订单审计、收益校验、随机事件统计。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 摆摊局ID | 摆摊主键 |
| `player_id` | BIGINT | 玩家ID | 局归属 |
| `session_no` | VARCHAR(64) | 摆摊局业务编号 | 幂等结算 |
| `status` | VARCHAR(32) | RUNNING、FINISHED、CANCELLED | 局状态机 |
| `started_at` | DATETIME | 开局时间 | 营业时长校验 |
| `ended_at` | DATETIME | 结束时间 | 结算记录 |
| `duration_seconds` | INT | 营业时长，单位秒 | 60秒MVP局 |
| `order_count` | INT | 订单总数 | 结算统计 |
| `success_order_count` | INT | 成功完成订单数 | 收益计算 |
| `combo_max` | INT | 最大连击数 | 连击奖励 |
| `thief_handled_count` | INT | 成功处理小偷次数 | 随机事件奖励 |
| `tycoon_completed_count` | INT | 完成财富大亨订单次数 | 爆发收益 |
| `coins_earned` | BIGINT | 本局获得金币 | 金币结算 |
| `client_payload` | JSON | 客户端上报明细 | 审计、排查作弊 |
| `server_payload` | JSON | 服务端校验明细 | 审计、排查作弊 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记 | 软删除 |

## economy_log

表说明：金币经济流水表。

使用场景：所有金币增减的追溯，包括出售、摆摊结算、广告奖励、升级消耗。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 经济流水ID | 流水主键 |
| `player_id` | BIGINT | 玩家ID | 玩家维度查询 |
| `change_type` | VARCHAR(64) | COIN_ADD、COIN_COST | 增减类型 |
| `amount` | BIGINT | 变动数量 | 流水金额 |
| `before_amount` | BIGINT | 变动前金币余额 | 审计 |
| `after_amount` | BIGINT | 变动后金币余额 | 审计 |
| `biz_type` | VARCHAR(64) | HARVEST、SELL_ITEM、STALL_FINISH、AD_REWARD、UPGRADE | 业务来源 |
| `biz_id` | VARCHAR(128) | 业务ID | 幂等和追溯 |
| `remark` | VARCHAR(255) | 备注说明 | 排查问题 |
| `created_at` | DATETIME | 创建时间 | 审计 |

## ad_reward_record

表说明：广告奖励领取记录表。

使用场景：广告奖励发放、防重复领取、每日次数限制。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 广告奖励记录ID | 主键 |
| `player_id` | BIGINT | 玩家ID | 归属 |
| `reward_type` | VARCHAR(64) | 奖励类型 | 区分双倍金币、立即成熟等 |
| `client_request_id` | VARCHAR(128) | 客户端请求ID | 防重复领取 |
| `reward_payload` | JSON | 奖励内容JSON | 发放明细 |
| `claimed_at` | DATETIME | 领取时间 | 次数限制 |

## achievement_progress

表说明：玩家成就进度表。

使用场景：农场成就、摆摊成就、奖励领取。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 成就进度ID | 主键 |
| `player_id` | BIGINT | 玩家ID | 归属 |
| `achievement_id` | VARCHAR(64) | 成就ID | 配置关联 |
| `progress` | BIGINT | 当前进度值 | 成就进度 |
| `completed` | TINYINT | 是否完成 | 成就状态 |
| `completed_at` | DATETIME | 完成时间 | 审计 |
| `reward_claimed` | TINYINT | 奖励是否已领取 | 防重复领取 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
| `deleted` | TINYINT | 逻辑删除标记 | 软删除 |

## game_config_version

表说明：游戏配置版本表。

使用场景：客户端判断配置是否需要更新，服务端配置发布追踪。

| 字段 | 类型 | 说明 | 场景 |
| --- | --- | --- | --- |
| `id` | BIGINT | 配置版本记录ID | 主键 |
| `config_type` | VARCHAR(64) | 配置类型：crop、recipe、upgrade、random_event等 | 配置分类 |
| `version` | VARCHAR(64) | 配置版本号 | 客户端更新判断 |
| `content_hash` | VARCHAR(128) | 配置内容哈希 | 防止重复下载 |
| `enabled` | TINYINT | 是否启用 | 配置开关 |
| `created_at` | DATETIME | 创建时间 | 审计 |
| `updated_at` | DATETIME | 更新时间 | 审计 |
