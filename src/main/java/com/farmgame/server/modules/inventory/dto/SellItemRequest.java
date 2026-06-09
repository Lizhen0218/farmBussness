package com.farmgame.server.modules.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "仓库物品售出请求")
public class SellItemRequest {

    @Schema(description = "要售出的物品ID，例如 tomato、corn、lettuce", example = "tomato")
    @NotBlank
    private String itemId;

    @Schema(description = "售出数量，必须大于0", example = "1")
    @Min(1)
    private int count;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
