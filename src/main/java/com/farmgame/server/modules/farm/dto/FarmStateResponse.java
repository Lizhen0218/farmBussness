package com.farmgame.server.modules.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "农场状态响应")
public class FarmStateResponse {

    @Schema(description = "玩家ID", example = "1890000000000000000")
    private Long playerId;

    @Schema(description = "服务端当前时间戳，毫秒", example = "1780915200000")
    private Long serverTime;

    @Schema(description = "土地状态列表")
    private List<LandState> lands;

    @Schema(description = "土地状态")
    public static class LandState {
        @Schema(description = "土地ID", example = "1890000000000000001")
        private Long landId;

        @Schema(description = "土地序号，从1开始", example = "1")
        private Integer landIndex;

        @Schema(description = "土地状态：EMPTY空地，GROWING种植中", example = "GROWING")
        private String status;

        @Schema(description = "当前种植作物ID；空地时为空", example = "tomato")
        private String cropId;

        @Schema(description = "播种时间，ISO-8601格式；未种植时为空", example = "2026-06-08T18:00:00")
        private String plantedAt;

        @Schema(description = "成熟时间，ISO-8601格式；未种植时为空", example = "2026-06-08T18:01:00")
        private String matureAt;

        @Schema(description = "当前是否已成熟", example = "false")
        private Boolean mature;

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

        public String getPlantedAt() {
            return plantedAt;
        }

        public void setPlantedAt(String plantedAt) {
            this.plantedAt = plantedAt;
        }

        public String getMatureAt() {
            return matureAt;
        }

        public void setMatureAt(String matureAt) {
            this.matureAt = matureAt;
        }

        public Boolean getMature() {
            return mature;
        }

        public void setMature(Boolean mature) {
            this.mature = mature;
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

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public List<LandState> getLands() {
        return lands;
    }

    public void setLands(List<LandState> lands) {
        this.lands = lands;
    }
}
