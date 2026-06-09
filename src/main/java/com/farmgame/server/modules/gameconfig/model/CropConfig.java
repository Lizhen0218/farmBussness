package com.farmgame.server.modules.gameconfig.model;

public class CropConfig {

    private String cropId;
    private String name;
    private Integer growTimeSeconds;
    private Integer sellPrice;
    private Integer exp;
    private Integer unlockLevel;

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrowTimeSeconds() {
        return growTimeSeconds;
    }

    public void setGrowTimeSeconds(Integer growTimeSeconds) {
        this.growTimeSeconds = growTimeSeconds;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getUnlockLevel() {
        return unlockLevel;
    }

    public void setUnlockLevel(Integer unlockLevel) {
        this.unlockLevel = unlockLevel;
    }
}
