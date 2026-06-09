package com.farmgame.server.modules.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "播种响应")
public class PlantResponse {

    @Schema(description = "土地ID", example = "1890000000000000001")
    private Long landId;

    @Schema(description = "作物ID", example = "tomato")
    private String cropId;

    @Schema(description = "播种时间，ISO-8601格式", example = "2026-06-08T18:00:00")
    private String plantedAt;

    @Schema(description = "成熟时间，ISO-8601格式", example = "2026-06-08T18:01:00")
    private String matureAt;

    public Long getLandId() {
        return landId;
    }

    public void setLandId(Long landId) {
        this.landId = landId;
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
}
