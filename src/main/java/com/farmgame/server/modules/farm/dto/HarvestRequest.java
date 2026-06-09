package com.farmgame.server.modules.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "收获请求")
public class HarvestRequest {

    @Schema(description = "土地ID，必须属于当前玩家且作物已成熟", example = "1890000000000000001")
    @NotNull
    private Long landId;

    public Long getLandId() {
        return landId;
    }

    public void setLandId(Long landId) {
        this.landId = landId;
    }
}
