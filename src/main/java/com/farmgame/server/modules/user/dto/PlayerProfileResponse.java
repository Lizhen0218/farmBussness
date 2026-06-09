package com.farmgame.server.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "玩家完整档案响应")
public class PlayerProfileResponse {

    @Schema(description = "玩家ID", example = "1890000000000000000")
    private Long playerId;

    @Schema(description = "玩家昵称", example = "测试农场主")
    private String nickName;

    @Schema(description = "玩家头像URL", example = "https://example.com/avatar.png")
    private String avatarUrl;

    @Schema(description = "玩家等级", example = "1")
    private Integer level;

    @Schema(description = "玩家经验值", example = "0")
    private Long exp;

    @Schema(description = "当前金币余额", example = "0")
    private Long coins;

    @Schema(description = "历史累计收入，用于总榜", example = "0")
    private Long totalIncome;

    @Schema(description = "当日收入，用于每日榜", example = "0")
    private Long dailyIncome;

    @Schema(description = "玩家土地摘要列表")
    private List<LandSummary> lands;

    @Schema(description = "玩家土地摘要")
    public static class LandSummary {
        @Schema(description = "土地ID", example = "1890000000000000001")
        private Long landId;

        @Schema(description = "土地序号，从1开始", example = "1")
        private Integer landIndex;

        @Schema(description = "土地状态：EMPTY空地，GROWING种植中", example = "EMPTY")
        private String status;

        @Schema(description = "当前种植作物ID；空地时为空", example = "tomato")
        private String cropId;

        @Schema(description = "成熟时间，ISO-8601格式；未种植时为空", example = "2026-06-08T18:00:00")
        private String matureAt;

        @Schema(description = "是否已解锁", example = "true")
        private Boolean unlocked;

        public Long getLandId() {
            return landId;
        }

        public void setLandId(Long landId) {
            this.landId = landId;
        }

        public Integer getLandIndex() {
            return landIndex;
        }

        public void setLandIndex(Integer landIndex) {
            this.landIndex = landIndex;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCropId() {
            return cropId;
        }

        public void setCropId(String cropId) {
            this.cropId = cropId;
        }

        public String getMatureAt() {
            return matureAt;
        }

        public void setMatureAt(String matureAt) {
            this.matureAt = matureAt;
        }

        public Boolean getUnlocked() {
            return unlocked;
        }

        public void setUnlocked(Boolean unlocked) {
            this.unlocked = unlocked;
        }
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getCoins() {
        return coins;
    }

    public void setCoins(Long coins) {
        this.coins = coins;
    }

    public Long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Long getDailyIncome() {
        return dailyIncome;
    }

    public void setDailyIncome(Long dailyIncome) {
        this.dailyIncome = dailyIncome;
    }

    public List<LandSummary> getLands() {
        return lands;
    }

    public void setLands(List<LandSummary> lands) {
        this.lands = lands;
    }
}
