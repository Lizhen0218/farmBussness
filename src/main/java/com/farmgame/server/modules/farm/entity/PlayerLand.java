package com.farmgame.server.modules.farm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 玩家土地状态实体，对应表 player_land。
 *
 * <p>用于农场状态查询、播种、成熟判断和收获。</p>
 */
@Schema(description = "玩家土地状态实体，对应表 player_land")
@TableName("player_land")
public class PlayerLand {

    /** 土地ID，雪花算法生成。 */
    @Schema(description = "土地ID，雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 玩家ID。 */
    @Schema(description = "玩家ID")
    @TableField("player_id")
    private Long playerId;

    /** 土地序号，从1开始，用于客户端展示排序。 */
    @Schema(description = "土地序号，从1开始，用于客户端展示排序")
    @TableField("land_index")
    private Integer landIndex;

    /** 土地状态：EMPTY空地，GROWING种植中。 */
    @Schema(description = "土地状态：EMPTY空地，GROWING种植中")
    @TableField("status")
    private String status;

    /** 当前种植作物ID，对应作物配置 cropId。 */
    @Schema(description = "当前种植作物ID，对应作物配置 cropId")
    @TableField("crop_id")
    private String cropId;

    /** 播种时间。 */
    @Schema(description = "播种时间")
    @TableField("planted_at")
    private LocalDateTime plantedAt;

    /** 成熟时间。 */
    @Schema(description = "成熟时间")
    @TableField("mature_at")
    private LocalDateTime matureAt;

    /** 是否已解锁：0未解锁，1已解锁。 */
    @Schema(description = "是否已解锁：0未解锁，1已解锁")
    @TableField("unlocked")
    private Integer unlocked;

    /** 创建时间。 */
    @Schema(description = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    /** 更新时间。 */
    @Schema(description = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /** 逻辑删除标记：0未删除，1已删除。 */
    @Schema(description = "逻辑删除标记：0未删除，1已删除")
    @TableField("deleted")
    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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

    public LocalDateTime getPlantedAt() {
        return plantedAt;
    }

    public void setPlantedAt(LocalDateTime plantedAt) {
        this.plantedAt = plantedAt;
    }

    public LocalDateTime getMatureAt() {
        return matureAt;
    }

    public void setMatureAt(LocalDateTime matureAt) {
        this.matureAt = matureAt;
    }

    public Integer getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(Integer unlocked) {
        this.unlocked = unlocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
