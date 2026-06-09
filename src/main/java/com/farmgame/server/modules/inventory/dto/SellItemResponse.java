package com.farmgame.server.modules.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "仓库物品售出响应")
public class SellItemResponse {

    @Schema(description = "售出的物品ID", example = "tomato")
    private String itemId;

    @Schema(description = "售出数量", example = "1")
    private Integer count;

    @Schema(description = "单个物品售出金币价格", example = "2")
    private Long unitPrice;

    @Schema(description = "本次获得金币", example = "2")
    private Long coinsAdded;

    @Schema(description = "售出后该物品剩余库存", example = "3")
    private Long remainingQuantity;

    @Schema(description = "售出后玩家金币余额", example = "120")
    private Long coinsBalance;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getCoinsAdded() {
        return coinsAdded;
    }

    public void setCoinsAdded(Long coinsAdded) {
        this.coinsAdded = coinsAdded;
    }

    public Long getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Long remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Long getCoinsBalance() {
        return coinsBalance;
    }

    public void setCoinsBalance(Long coinsBalance) {
        this.coinsBalance = coinsBalance;
    }
}
