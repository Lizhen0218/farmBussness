-- 农场经营 + 摆摊小游戏后端完整初始化脚本
-- 用途：本地开发或测试环境需要“覆盖重建表结构”时手工执行。
-- 注意：该脚本会删除已有业务表并重建，执行前请确认数据可以丢弃。

CREATE DATABASE IF NOT EXISTS farm_game
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_0900_ai_ci
    COMMENT '农场经营摆摊小游戏数据库';

USE farm_game;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS achievement_progress;
DROP TABLE IF EXISTS ad_reward_record;
DROP TABLE IF EXISTS economy_log;
DROP TABLE IF EXISTS stall_session;
DROP TABLE IF EXISTS player_upgrade;
DROP TABLE IF EXISTS player_inventory;
DROP TABLE IF EXISTS player_land;
DROP TABLE IF EXISTS game_config_version;
DROP TABLE IF EXISTS player;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE player (
    id BIGINT NOT NULL COMMENT '玩家ID，雪花算法生成',
    open_id VARCHAR(128) NOT NULL COMMENT '微信 openId；开发期使用 dev_code',
    nick_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '玩家昵称',
    avatar_url VARCHAR(512) NOT NULL DEFAULT '' COMMENT '玩家头像URL',
    level INT NOT NULL DEFAULT 1 COMMENT '玩家等级',
    exp BIGINT NOT NULL DEFAULT 0 COMMENT '玩家经验值',
    coins BIGINT NOT NULL DEFAULT 0 COMMENT '当前金币余额',
    total_income BIGINT NOT NULL DEFAULT 0 COMMENT '历史累计收入，用于总榜',
    daily_income BIGINT NOT NULL DEFAULT 0 COMMENT '当日收入，用于每日榜',
    last_login_at DATETIME NULL COMMENT '最近登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_player_open_id (open_id),
    KEY idx_player_daily_income (daily_income),
    KEY idx_player_total_income (total_income)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家基础信息表';

CREATE TABLE player_land (
    id BIGINT NOT NULL COMMENT '土地ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    land_index INT NOT NULL COMMENT '土地序号，从1开始，用于客户端展示排序',
    status VARCHAR(32) NOT NULL DEFAULT 'EMPTY' COMMENT '土地状态：EMPTY空地，GROWING种植中',
    crop_id VARCHAR(64) NULL COMMENT '当前种植作物ID，对应作物配置 cropId',
    planted_at DATETIME NULL COMMENT '播种时间',
    mature_at DATETIME NULL COMMENT '成熟时间',
    unlocked TINYINT NOT NULL DEFAULT 1 COMMENT '是否已解锁：0未解锁，1已解锁',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_player_land_index (player_id, land_index),
    KEY idx_player_land_player (player_id),
    KEY idx_player_land_mature (player_id, mature_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家土地状态表';

CREATE TABLE player_inventory (
    id BIGINT NOT NULL COMMENT '仓库记录ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    item_id VARCHAR(64) NOT NULL COMMENT '物品ID，例如 tomato、corn、lettuce',
    item_type VARCHAR(32) NOT NULL COMMENT '物品类型：CROP作物，MATERIAL材料，PROP道具',
    quantity BIGINT NOT NULL DEFAULT 0 COMMENT '物品数量',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_player_item (player_id, item_id),
    KEY idx_player_inventory_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家仓库物品表';

CREATE TABLE player_upgrade (
    id BIGINT NOT NULL COMMENT '升级记录ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    upgrade_type VARCHAR(32) NOT NULL COMMENT '升级类型：FARM农场，STALL摊位，WAREHOUSE仓库，DECORATION装饰',
    target_id VARCHAR(64) NOT NULL COMMENT '升级目标ID，例如 stall_main、warehouse_main',
    level INT NOT NULL DEFAULT 1 COMMENT '当前等级',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_player_upgrade_target (player_id, upgrade_type, target_id),
    KEY idx_player_upgrade_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家升级状态表';

CREATE TABLE stall_session (
    id BIGINT NOT NULL COMMENT '摆摊局ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    session_no VARCHAR(64) NOT NULL COMMENT '摆摊局业务编号，用于幂等结算',
    status VARCHAR(32) NOT NULL DEFAULT 'RUNNING' COMMENT '局状态：RUNNING进行中，FINISHED已结算，CANCELLED已取消',
    started_at DATETIME NOT NULL COMMENT '开局时间',
    ended_at DATETIME NULL COMMENT '结束时间',
    duration_seconds INT NOT NULL DEFAULT 60 COMMENT '营业时长，单位秒',
    order_count INT NOT NULL DEFAULT 0 COMMENT '订单总数',
    success_order_count INT NOT NULL DEFAULT 0 COMMENT '成功完成订单数',
    combo_max INT NOT NULL DEFAULT 0 COMMENT '最大连击数',
    thief_handled_count INT NOT NULL DEFAULT 0 COMMENT '成功处理小偷事件次数',
    tycoon_completed_count INT NOT NULL DEFAULT 0 COMMENT '完成财富大亨订单次数',
    coins_earned BIGINT NOT NULL DEFAULT 0 COMMENT '本局获得金币',
    client_payload JSON NULL COMMENT '客户端上报结算明细JSON，用于审计',
    server_payload JSON NULL COMMENT '服务端校验结算明细JSON，用于审计',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_stall_session_no (session_no),
    KEY idx_stall_session_player (player_id, started_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='摆摊营业局记录表';

CREATE TABLE economy_log (
    id BIGINT NOT NULL COMMENT '经济流水ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    change_type VARCHAR(64) NOT NULL COMMENT '变动类型：COIN_ADD金币增加，COIN_COST金币消耗',
    amount BIGINT NOT NULL COMMENT '变动数量，正数表示增加或消耗的绝对值',
    before_amount BIGINT NOT NULL COMMENT '变动前金币余额',
    after_amount BIGINT NOT NULL COMMENT '变动后金币余额',
    biz_type VARCHAR(64) NOT NULL COMMENT '业务类型：HARVEST、SELL_ITEM、STALL_FINISH、AD_REWARD、UPGRADE',
    biz_id VARCHAR(128) NOT NULL COMMENT '业务ID，用于幂等和追溯',
    remark VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注说明',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_economy_log_player_time (player_id, created_at),
    KEY idx_economy_log_biz (biz_type, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='金币经济流水表';

CREATE TABLE ad_reward_record (
    id BIGINT NOT NULL COMMENT '广告奖励记录ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    reward_type VARCHAR(64) NOT NULL COMMENT '奖励类型：DOUBLE_STALL、INSTANT_MATURE、DAILY_GIFT等',
    client_request_id VARCHAR(128) NOT NULL COMMENT '客户端请求ID，用于防重复领取',
    reward_payload JSON NULL COMMENT '奖励内容JSON',
    claimed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_ad_reward_request (player_id, client_request_id),
    KEY idx_ad_reward_player_type_time (player_id, reward_type, claimed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='广告奖励领取记录表';

CREATE TABLE achievement_progress (
    id BIGINT NOT NULL COMMENT '成就进度ID，雪花算法生成',
    player_id BIGINT NOT NULL COMMENT '玩家ID',
    achievement_id VARCHAR(64) NOT NULL COMMENT '成就ID，对应成就配置',
    progress BIGINT NOT NULL DEFAULT 0 COMMENT '当前进度值',
    completed TINYINT NOT NULL DEFAULT 0 COMMENT '是否完成：0未完成，1已完成',
    completed_at DATETIME NULL COMMENT '完成时间',
    reward_claimed TINYINT NOT NULL DEFAULT 0 COMMENT '奖励是否已领取：0未领取，1已领取',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_player_achievement (player_id, achievement_id),
    KEY idx_achievement_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家成就进度表';

CREATE TABLE game_config_version (
    id BIGINT NOT NULL COMMENT '配置版本记录ID，雪花算法生成',
    config_type VARCHAR(64) NOT NULL COMMENT '配置类型：crop、recipe、upgrade、random_event等',
    version VARCHAR(64) NOT NULL COMMENT '配置版本号',
    content_hash VARCHAR(128) NOT NULL COMMENT '配置内容哈希，用于客户端判断是否更新',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_type_version (config_type, version),
    KEY idx_config_enabled (config_type, enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='游戏配置版本表';

INSERT INTO game_config_version (id, config_type, version, content_hash, enabled)
VALUES
    (1001, 'crop', 'mvp-0.1.0', 'todo-crop-json-hash', 1),
    (1002, 'recipe', 'mvp-0.1.0', 'todo-recipe-json-hash', 1),
    (1003, 'upgrade', 'mvp-0.1.0', 'todo-upgrade-json-hash', 1),
    (1004, 'random_event', 'mvp-0.1.0', 'todo-random-event-json-hash', 1)
ON DUPLICATE KEY UPDATE
    content_hash = VALUES(content_hash),
    enabled = VALUES(enabled),
    updated_at = CURRENT_TIMESTAMP;
