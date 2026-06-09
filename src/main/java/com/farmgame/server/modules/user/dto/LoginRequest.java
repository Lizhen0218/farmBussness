package com.farmgame.server.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "登录请求")
public class LoginRequest {

    @Schema(description = "微信登录临时 code；开发期用于模拟 openId", example = "dev001")
    @NotBlank
    private String code;

    @Schema(description = "玩家昵称，首次登录时写入玩家档案", example = "测试农场主")
    private String nickName;

    @Schema(description = "玩家头像URL，首次登录或重复登录时刷新", example = "https://example.com/avatar.png")
    private String avatarUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
