package com.farmgame.server.modules.economy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 金币流水实体，对应表 economy_log。
 *
 * <p>用于记录出售、摆摊、广告等奖励导致的金币余额变化，保障经济系统可追溯。</p>
 */
@Schema(description = "金币流水实体，对应表 economy_log")
@TableName("economy_log")
public class EconomyLog {

    /** 流水ID，雪花算法生成。 */
    @Schema(description = "流水ID，雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 玩家ID。 */
    @Schema(description = "玩家ID")
    @TableField("player_id")
    private Long playerId;

    /** 变动类型，例如 ADD、DEDUCT。 */
    @Schema(description = "变动类型，例如 ADD、DEDUCT")
    @TableField("change_type")
    private String changeType;

    /** 本次变动金币数量。 */
    @Schema(description = "本次变动金币数量")
    @TableField("amount")
    private Long amount;

    /** 变动前金币余额。 */
    @Schema(description = "变动前金币余额")
    @TableField("before_amount")
    private Long beforeAmount;

    /** 变动后金币余额。 */
    @Schema(description = "变动后金币余额")
    @TableField("after_amount")
    private Long afterAmount;

    /** 业务类型，例如 INVENTORY_SELL。 */
    @Schema(description = "业务类型，例如 INVENTORY_SELL")
    @TableField("biz_type")
    private String bizType;

    /** 业务ID，用于追踪来源。 */
    @Schema(description = "业务ID，用于追踪来源")
    @TableField("biz_id")
    private String bizId;

    /** 备注。 */
    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    /** 创建时间。 */
    @Schema(description = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

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

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Long beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Long getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(Long afterAmount) {
        this.afterAmount = afterAmount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
