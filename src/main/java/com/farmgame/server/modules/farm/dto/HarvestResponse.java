package com.farmgame.server.modules.farm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "收获响应")
public class HarvestResponse {

    @Schema(description = "土地ID", example = "1890000000000000001")
    private Long landId;

    @Schema(description = "收获物品ID", example = "tomato")
    private String itemId;

    @Schema(description = "收获物品名称", example = "番茄")
    private String itemName;

    @Schema(description = "本次收获数量", example = "1")
    private Integer count;

    @Schema(description = "收获后该物品在仓库中的总数量", example = "3")
    private Long inventoryQuantity;

    public Long getLandId() {
        return landId;
    }

    public void setLandId(Long landId) {
        this.landId = landId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Long inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }
}
