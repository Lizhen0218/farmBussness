package com.farmgame.server.modules.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "播种请求")
public class PlantRequest {

    @Schema(description = "土地ID，必须属于当前玩家且为空地", example = "1890000000000000001")
    @NotNull
    private Long landId;

    @Schema(description = "作物ID，对应作物配置 cropId", example = "tomato")
    @NotBlank
    private String cropId;

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
}
