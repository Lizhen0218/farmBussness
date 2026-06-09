package com.farmgame.server.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 玩家基础信息实体，对应表 player。
 *
 * <p>用于登录、玩家档案、金币余额、等级经验和排行榜收入统计。</p>
 */
@Schema(description = "玩家基础信息实体，对应表 player")
@TableName("player")
public class Player {

    /** 玩家ID，雪花算法生成。 */
    @Schema(description = "玩家ID，雪花算法生成")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 微信 openId；开发期使用 dev_code。 */
    @Schema(description = "微信 openId；开发期使用 dev_code")
    @TableField("open_id")
    private String openId;

    /** 玩家昵称。 */
    @Schema(description = "玩家昵称")
    @TableField("nick_name")
    private String nickName;

    /** 玩家头像URL。 */
    @Schema(description = "玩家头像URL")
    @TableField("avatar_url")
    private String avatarUrl;

    /** 玩家等级。 */
    @Schema(description = "玩家等级")
    @TableField("level")
    private Integer level;

    /** 玩家经验值。 */
    @Schema(description = "玩家经验值")
    @TableField("exp")
    private Long exp;

    /** 当前金币余额。 */
    @Schema(description = "当前金币余额")
    @TableField("coins")
    private Long coins;

    /** 历史累计收入，用于总榜。 */
    @Schema(description = "历史累计收入，用于总榜")
    @TableField("total_income")
    private Long totalIncome;

    /** 当日收入，用于每日榜。 */
    @Schema(description = "当日收入，用于每日榜")
    @TableField("daily_income")
    private Long dailyIncome;

    /** 最近登录时间。 */
    @Schema(description = "最近登录时间")
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public Long getCoins() {
        return coins;
    }

    public void setCoins(Long coins) {
        this.coins = coins;
    }

    public Long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Long getDailyIncome() {
        return dailyIncome;
    }

    public void setDailyIncome(Long dailyIncome) {
        this.dailyIncome = dailyIncome;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
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
