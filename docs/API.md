# API 说明

## Swagger 地址

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

## MVP 接口

开发期除登录外，请求需要带请求头：

```text
X-Player-Id: 登录接口返回的 playerId
```

| 模块 | 方法 | 路径 | 说明 |
| --- | --- | --- | --- |
| 用户 | POST | `/api/user/login` | 微信小游戏登录 |
| 玩家 | GET | `/api/player/profile` | 获取玩家完整数据 |
| 农场 | GET | `/api/farm/state` | 获取农场状态 |
| 农场 | POST | `/api/farm/plant` | 播种 |
| 农场 | POST | `/api/farm/harvest` | 收获 |
| 仓库 | GET | `/api/inventory/list` | 获取仓库 |
| 仓库 | POST | `/api/inventory/sell` | 直接出售物品 |
| 摆摊 | POST | `/api/stall/start` | 开始摆摊 |
| 摆摊 | POST | `/api/stall/finish` | 结束摆摊并结算 |
| 排行榜 | GET | `/api/leaderboard/income` | 获取收入排行榜 |
| 广告 | POST | `/api/ad/reward` | 领取广告奖励 |
| 配置 | GET | `/api/config/version` | 获取配置版本 |
| 配置 | GET | `/api/config/crops` | 获取作物配置 |

## 统一返回

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": 1780915200000
}
```

## Apifox/Postman 最小测试顺序

1. 登录：

```http
POST /api/user/login
Content-Type: application/json

{
  "code": "dev001",
  "nickName": "测试农场主",
  "avatarUrl": ""
}
```

2. 复制返回的 `playerId`，后续请求加请求头：

```text
X-Player-Id: {playerId}
```

3. 查询玩家档案：

```http
GET /api/player/profile
X-Player-Id: {playerId}
```

4. 查询农场状态：

```http
GET /api/farm/state
X-Player-Id: {playerId}
```

5. 查询作物配置：

```http
GET /api/config/crops
```

6. 选择一块空土地播种：

```http
POST /api/farm/plant
Content-Type: application/json
X-Player-Id: {playerId}

{
  "landId": "{landId}",
  "cropId": "tomato"
}
```

7. 作物成熟后收获：

```http
POST /api/farm/harvest
Content-Type: application/json
X-Player-Id: {playerId}

{
  "landId": "{landId}"
}
```

8. 查询仓库：

```http
GET /api/inventory/list
X-Player-Id: {playerId}
```
