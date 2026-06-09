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
