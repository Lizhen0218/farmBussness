package com.farmgame.server.common.api;

public enum ErrorCode {
    BAD_REQUEST(400, "请求参数不正确"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "请求状态冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    PLAYER_NOT_FOUND(10001, "玩家不存在"),
    LAND_NOT_FOUND(20001, "土地不存在"),
    LAND_NOT_EMPTY(20002, "土地已有作物"),
    CROP_NOT_MATURE(20003, "作物尚未成熟"),
    INVENTORY_NOT_ENOUGH(30001, "仓库物品不足"),
    COIN_NOT_ENOUGH(40001, "金币不足"),
    STALL_SESSION_NOT_FOUND(50001, "摆摊局不存在"),
    REWARD_ALREADY_CLAIMED(60001, "奖励已领取");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
