package com.farmgame.server.modules.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 玩家仓库物品实体，对应表 player_inventory。
 *
 * <p>用于收获入仓、仓库列表、直接出售和摆摊材料消耗。</p>
 */
@Schema(description = "玩家仓库物品实体，对应表 player_inventory")
@TableName("player_inventory")
public class PlayerInventory {

    /** 仓库记录ID，雪花算法生成。 */
    @Schema(description = "仓库记录ID，雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 玩家ID。 */
    @Schema(description = "玩家ID")
    @TableField("player_id")
    private Long playerId;

    /** 物品ID，例如 tomato、corn、lettuce。 */
    @Schema(description = "物品ID，例如 tomato、corn、lettuce")
    @TableField("item_id")
    private String itemId;

    /** 物品类型：CROP作物，MATERIAL材料，PROP道具。 */
    @Schema(description = "物品类型：CROP作物，MATERIAL材料，PROP道具")
    @TableField("item_type")
    private String itemType;

    /** 物品数量。 */
    @Schema(description = "物品数量")
    @TableField("quantity")
    private Long quantity;

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
