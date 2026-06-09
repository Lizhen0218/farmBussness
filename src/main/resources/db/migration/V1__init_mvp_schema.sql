CREATE TABLE player (
    id BIGINT NOT NULL PRIMARY KEY,
    open_id VARCHAR(128) NOT NULL,
    nick_name VARCHAR(64) NOT NULL DEFAULT '',
    avatar_url VARCHAR(512) NOT NULL DEFAULT '',
    level INT NOT NULL DEFAULT 1,
    exp BIGINT NOT NULL DEFAULT 0,
    coins BIGINT NOT NULL DEFAULT 0,
    total_income BIGINT NOT NULL DEFAULT 0,
    daily_income BIGINT NOT NULL DEFAULT 0,
    last_login_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_player_open_id (open_id),
    KEY idx_player_daily_income (daily_income),
    KEY idx_player_total_income (total_income)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE player_land (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    land_index INT NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'EMPTY',
    crop_id VARCHAR(64) NULL,
    planted_at DATETIME NULL,
    mature_at DATETIME NULL,
    unlocked TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_player_land_index (player_id, land_index),
    KEY idx_player_land_player (player_id),
    KEY idx_player_land_mature (player_id, mature_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE player_inventory (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    item_id VARCHAR(64) NOT NULL,
    item_type VARCHAR(32) NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_player_item (player_id, item_id),
    KEY idx_player_inventory_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE player_upgrade (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    upgrade_type VARCHAR(32) NOT NULL,
    target_id VARCHAR(64) NOT NULL,
    level INT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_player_upgrade_target (player_id, upgrade_type, target_id),
    KEY idx_player_upgrade_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE stall_session (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    session_no VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'RUNNING',
    started_at DATETIME NOT NULL,
    ended_at DATETIME NULL,
    duration_seconds INT NOT NULL DEFAULT 60,
    order_count INT NOT NULL DEFAULT 0,
    success_order_count INT NOT NULL DEFAULT 0,
    combo_max INT NOT NULL DEFAULT 0,
    thief_handled_count INT NOT NULL DEFAULT 0,
    tycoon_completed_count INT NOT NULL DEFAULT 0,
    coins_earned BIGINT NOT NULL DEFAULT 0,
    client_payload JSON NULL,
    server_payload JSON NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_stall_session_no (session_no),
    KEY idx_stall_session_player (player_id, started_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE economy_log (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    change_type VARCHAR(64) NOT NULL,
    amount BIGINT NOT NULL,
    before_amount BIGINT NOT NULL,
    after_amount BIGINT NOT NULL,
    biz_type VARCHAR(64) NOT NULL,
    biz_id VARCHAR(128) NOT NULL,
    remark VARCHAR(255) NOT NULL DEFAULT '',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_economy_log_player_time (player_id, created_at),
    KEY idx_economy_log_biz (biz_type, biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE ad_reward_record (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    reward_type VARCHAR(64) NOT NULL,
    client_request_id VARCHAR(128) NOT NULL,
    reward_payload JSON NULL,
    claimed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_ad_reward_request (player_id, client_request_id),
    KEY idx_ad_reward_player_type_time (player_id, reward_type, claimed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE achievement_progress (
    id BIGINT NOT NULL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    achievement_id VARCHAR(64) NOT NULL,
    progress BIGINT NOT NULL DEFAULT 0,
    completed TINYINT NOT NULL DEFAULT 0,
    completed_at DATETIME NULL,
    reward_claimed TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_player_achievement (player_id, achievement_id),
    KEY idx_achievement_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE game_config_version (
    id BIGINT NOT NULL PRIMARY KEY,
    config_type VARCHAR(64) NOT NULL,
    version VARCHAR(64) NOT NULL,
    content_hash VARCHAR(128) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_config_type_version (config_type, version),
    KEY idx_config_enabled (config_type, enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
