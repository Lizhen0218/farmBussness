package com.farmgame.server.modules.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "仓库物品响应")
public class InventoryItemResponse {

    @Schema(description = "物品ID，例如 tomato、corn、lettuce", example = "tomato")
    private String itemId;

    @Schema(description = "物品类型：CROP作物，MATERIAL材料，PROP道具", example = "CROP")
    private String itemType;

    @Schema(description = "当前仓库数量", example = "3")
    private Long quantity;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
