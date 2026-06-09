package com.farmgame.server.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "开发期访问令牌；后续替换为正式登录态", example = "dev-token-1890000000000000000")
    private String token;

    @Schema(description = "玩家ID，后续开发期接口通过 X-Player-Id 请求头传入", example = "1890000000000000000")
    private Long playerId;

    @Schema(description = "是否为首次创建的新玩家", example = "true")
    private Boolean newPlayer;

    public LoginResponse(String token, Long playerId, Boolean newPlayer) {
        this.token = token;
        this.playerId = playerId;
        this.newPlayer = newPlayer;
    }

    public String getToken() {
        return token;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Boolean getNewPlayer() {
        return newPlayer;
    }
}
