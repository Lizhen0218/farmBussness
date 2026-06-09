# 业务模块说明与 Controller 使用场景

> 维护要求：新增、删除、重命名业务模块或 Controller 方法时，必须同步更新本文档、[API.md](API.md)、[MVP_OUTLINE.md](MVP_OUTLINE.md)、[DOCUMENT_INDEX.md](DOCUMENT_INDEX.md) 和 [../logs/CHANGELOG.md](../logs/CHANGELOG.md)。

## 模块总览

| 模块 | 业务定位 | 当前状态 | 关联数据 |
| --- | --- | --- | --- |
| `user` | 玩家登录、玩家身份、玩家档案 | 已实现第一版 | `player`、`player_land` |
| `farm` | 农场土地、播种、成熟、收获 | 已实现第一版 | `player_land`、`player_inventory`、`crops.json` |
| `inventory` | 仓库物品、材料数量、直接出售入口 | 已实现列表、入仓和直接出售第一版 | `player_inventory`、`player`、`economy_log` |
| `gameconfig` | 游戏配置加载和配置下发 | 已实现作物配置第一版 | `game-config/crops.json`、`game_config_version` |
| `stall` | 摆摊开局、订单制作、结算 | 当前为接口占位 | `stall_session`、`player_inventory`、`economy_log` |
| `economy` | 金币余额、金币流水、经济校验 | 已实现直接出售流水第一版 | `player`、`economy_log` |
| `leaderboard` | 金币收入榜、每日榜、好友榜 | 当前为接口占位 | Redis Sorted Set、`player.total_income`、`player.daily_income` |
| `ad` | 广告激励奖励、防重复领取 | 当前为接口占位 | `ad_reward_record`、`economy_log` |
| `health` | 服务健康检查 | 已实现 | 无业务表 |

## 调用链视图

当前已经打通的后端最小链路：

```text
登录创建玩家
  -> 初始化 6 块土地
  -> 查询农场状态
  -> 查询作物配置
  -> 播种
  -> 等待成熟
  -> 收获
  -> 作物进入仓库
  -> 查询仓库
```

下一段计划打通：

```text
仓库物品
  -> 直接出售
  -> 金币增加
  -> economy_log 记录流水
```

## user 模块

业务作用：

`user` 模块负责玩家身份入口和玩家档案读取。MVP 开发期暂时用登录请求中的 `code` 模拟微信 openId；后续接微信小游戏时，会替换为微信登录凭证校验。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `UserController` | 用户登录入口 |
| `PlayerController` | 玩家档案查询入口 |
| `PlayerService` | 登录、创建玩家、刷新档案、查询玩家档案 |
| `Player` | 玩家基础信息实体，对应 `player` 表 |
| `PlayerMapper` | 玩家表 MyBatis-Plus Mapper |
| `dto` | 登录和玩家档案接口请求/响应对象 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `UserController.login` | `POST /api/user/login` | 玩家进入游戏时调用。用于创建或获取玩家身份。 | 请求体包含 `code`。开发期 `code` 会映射为 `dev_{code}` openId。 | 首次登录创建 `player`，并初始化 6 块 `player_land`；返回 `playerId` 和开发期 token。 |
| `PlayerController.profile` | `GET /api/player/profile` | 客户端进入主页、刷新玩家信息、调试完整档案时调用。 | 请求头包含 `X-Player-Id`。 | 返回玩家等级、经验、金币、收入统计和土地摘要。 |

测试建议：

1. 先调用 `POST /api/user/login`。
2. 复制返回的 `playerId`。
3. 调用 `GET /api/player/profile`，请求头带 `X-Player-Id`。

## farm 模块

业务作用：

`farm` 模块负责长期经营部分的核心产出。玩家通过土地播种作物，等待成熟后收获，作物进入仓库，成为摆摊制作和直接出售的原材料。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `FarmController` | 农场状态、播种、收获接口入口 |
| `FarmService` | 土地初始化、土地查询、播种校验、成熟校验、收获入仓 |
| `PlayerLand` | 玩家土地状态实体，对应 `player_land` 表 |
| `PlayerLandMapper` | 玩家土地表 Mapper |
| `dto` | 农场状态、播种、收获接口请求/响应对象 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `FarmController.state` | `GET /api/farm/state` | 农场主页加载、刷新土地状态、客户端倒计时校准时调用。 | 请求头包含 `X-Player-Id`。 | 返回服务端时间、土地列表、作物ID、播种时间、成熟时间、是否成熟。 |
| `FarmController.plant` | `POST /api/farm/plant` | 玩家点击空地并选择作物后调用。 | 土地属于当前玩家、土地已解锁、土地为空、作物配置存在。 | 写入土地状态为 `GROWING`，记录 `crop_id`、`planted_at`、`mature_at`。 |
| `FarmController.harvest` | `POST /api/farm/harvest` | 玩家点击成熟作物收获时调用。 | 土地属于当前玩家、土地正在种植、作物已成熟。 | 作物数量加到 `player_inventory`，土地重置为 `EMPTY`。 |

测试建议：

1. `GET /api/farm/state` 获取土地ID。
2. `GET /api/config/crops` 获取作物ID。
3. `POST /api/farm/plant` 播种。
4. 等待作物成熟。
5. `POST /api/farm/harvest` 收获。
6. `GET /api/inventory/list` 验证作物入仓。

## inventory 模块

业务作用：

`inventory` 模块负责玩家仓库。仓库承接农场产出，并为直接出售、摆摊制作食物、后续订单需求提供材料数量校验。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `InventoryController` | 仓库列表和直接出售接口入口 |
| `InventoryService` | 仓库物品累加、仓库列表转换 |
| `PlayerInventory` | 玩家仓库物品实体，对应 `player_inventory` 表 |
| `PlayerInventoryMapper` | 玩家仓库表 Mapper |
| `dto` | 仓库物品接口响应对象 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `InventoryController.list` | `GET /api/inventory/list` | 仓库界面打开、收获后刷新、摆摊前检查材料时调用。 | 请求头包含 `X-Player-Id`。 | 返回玩家当前仓库物品ID、类型和数量。 |
| `InventoryController.sell` | `POST /api/inventory/sell` | 玩家选择将作物直接卖给系统商店时调用。 | 请求头包含 `X-Player-Id`；请求体传 `itemId` 和 `count`；物品必须存在且数量足够。 | 扣减仓库数量，按作物配置售价增加金币，更新玩家累计收入并写入 `economy_log`。 |

测试建议：

当前先通过农场收获制造仓库数据，再调用 `GET /api/inventory/list` 查看数量，随后调用 `POST /api/inventory/sell` 验证库存扣减和金币增加。

## gameconfig 模块

业务作用：

`gameconfig` 模块负责游戏配置加载和下发。MVP 阶段先使用本地 JSON，后续可扩展为 Excel 转 JSON、后台配置或版本热更新。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `GameConfigController` | 配置版本和配置内容查询接口 |
| `GameConfigService` | 启动时加载 JSON 配置，提供配置查询 |
| `CropConfig` | 作物配置模型 |
| `src/main/resources/game-config/crops.json` | MVP 作物配置 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `GameConfigController.version` | `GET /api/config/version` | 客户端启动时判断配置版本，决定是否刷新本地配置缓存。 | 无。 | 返回当前配置版本号。 |
| `GameConfigController.crops` | `GET /api/config/crops` | 客户端播种面板展示可种作物，或测试作物配置时调用。 | 无。 | 返回作物ID、名称、成熟时间、售价、经验和解锁等级。 |

测试建议：

调用 `GET /api/config/crops`，确认返回 `tomato`、`corn`、`lettuce` 三种作物。

## stall 模块

业务作用：

`stall` 模块负责短局爽感核心：开始摆摊、生成订单、制作食物、处理随机事件、结束结算。当前为接口占位，后续会成为 MVP 的第二个核心模块。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `StallController` | 摆摊开始和结束结算接口入口 |
| `stall_session` 表 | 记录摆摊局、结算数据、客户端/服务端审计明细 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `StallController.start` | `POST /api/stall/start` | 玩家点击开始营业时调用。 | 后续需要校验玩家状态、摊位等级、材料是否足够。 | 当前返回开发期 `sessionId`；后续会创建 `stall_session`，生成本局订单和随机事件参数。 |
| `StallController.finish` | `POST /api/stall/finish` | 一局 60 秒营业结束后客户端提交结算时调用。 | 后续需要校验 `sessionId`、订单、材料消耗、随机事件奖励和金币收益。 | 当前为占位；后续会发放金币、扣材料、写 `stall_session` 和 `economy_log`。 |

测试建议：

当前仅用于 Swagger 占位查看，不作为真实业务测试通过标准。

## economy 模块

业务作用：

`economy` 模块负责金币余额、金币流水和经济校验。它是防止客户端篡改收益的关键模块。

当前状态：

目录已预留，Controller 和 Service 待实现。

后续职责：

| 职责 | 说明 |
| --- | --- |
| 金币增加 | 直接出售、摆摊结算、广告奖励 |
| 金币消耗 | 土地解锁、摊位升级、建筑升级 |
| 流水记录 | 所有金币变动必须写 `economy_log` |
| 幂等校验 | 同一个业务ID不能重复发金币或重复扣金币 |

## leaderboard 模块

业务作用：

`leaderboard` 模块负责排行榜，满足微信小游戏的轻社交刺激：每日收入榜、总收入榜、好友对比。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `LeaderboardController` | 排行榜查询接口入口 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `LeaderboardController.income` | `GET /api/leaderboard/income` | 玩家打开排行榜、结算后查看排名时调用。 | 当前为占位；后续 `type=daily/total` 区分日榜和总榜。 | 当前返回空列表；后续从 Redis Sorted Set 查询收入排名。 |

## ad 模块

业务作用：

`ad` 模块负责激励广告奖励发放和防重复领取。广告应该是加速和奖励，不是强制阻断。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `AdController` | 广告奖励领取接口入口 |
| `ad_reward_record` 表 | 广告奖励领取记录和幂等控制 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `AdController.reward` | `POST /api/ad/reward` | 玩家看完激励广告后领取奖励时调用。 | 当前为占位；后续需要校验广告完成凭证、奖励类型、次数限制和 `clientRequestId`。 | 当前返回未领取占位数据；后续会发奖励、写 `ad_reward_record`，必要时写 `economy_log`。 |

## health 模块

业务作用：

`health` 模块用于确认服务是否正常启动，不参与游戏业务。

目录职责：

| 子目录/文件 | 作用 |
| --- | --- |
| `HealthController` | 服务存活检查接口 |

Controller 方法：

| 方法 | 路径 | 业务场景 | 前置条件 | 结果影响 |
| --- | --- | --- | --- | --- |
| `HealthController.health` | `GET /api/health` | 本地启动后、部署后、监控探活时调用。 | 无。 | 返回 `status=UP` 和服务端时间。 |

## 后续新增模块时的文档同步规则

新增或修改业务模块时，需要同步：

1. 本文档：补模块作用、目录职责、Controller 方法使用场景。
2. [API.md](API.md)：补接口路径、请求、响应和测试顺序。
3. [MVP_OUTLINE.md](MVP_OUTLINE.md)：补进度、测试入口和模块职责。
4. [DOCUMENT_INDEX.md](DOCUMENT_INDEX.md)：如果新增文档、SQL、配置或脚本，要补索引。
5. [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)：如果新增表或字段，要补数据库字典。
6. [../logs/CHANGELOG.md](../logs/CHANGELOG.md)：记录完成时间、修改文件、验证结果和回退建议。
