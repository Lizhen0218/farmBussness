package com.farmgame.server.modules.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.farmgame.server.common.api.ErrorCode;
import com.farmgame.server.common.exception.BizException;
import com.farmgame.server.modules.farm.entity.PlayerLand;
import com.farmgame.server.modules.farm.service.FarmService;
import com.farmgame.server.modules.user.dto.LoginRequest;
import com.farmgame.server.modules.user.dto.LoginResponse;
import com.farmgame.server.modules.user.dto.PlayerProfileResponse;
import com.farmgame.server.modules.user.entity.Player;
import com.farmgame.server.modules.user.mapper.PlayerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerMapper playerMapper;
    private final FarmService farmService;

    public PlayerService(PlayerMapper playerMapper, FarmService farmService) {
        this.playerMapper = playerMapper;
        this.farmService = farmService;
    }

    /**
     * 登录并返回开发期 token。
     *
     * <p>MVP 开发阶段暂时将微信登录 code 归一化为 openId。首次登录会创建玩家，
     * 并初始化 6 块默认土地；重复登录会刷新昵称、头像和最近登录时间。</p>
     *
     * @param request 登录请求
     * @return 登录结果，包含 playerId、开发期 token 和是否新玩家
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        String openId = normalizeDevOpenId(request.getCode());
        Player player = playerMapper.selectOne(new LambdaQueryWrapper<Player>()
                .eq(Player::getOpenId, openId)
                .eq(Player::getDeleted, 0)
                .last("LIMIT 1"));

        boolean newPlayer = false;
        LocalDateTime now = LocalDateTime.now();
        if (player == null) {
            newPlayer = true;
            player = createPlayer(openId, request, now);
            playerMapper.insert(player);
            farmService.initDefaultLands(player.getId());
        } else {
            player.setNickName(defaultString(request.getNickName(), player.getNickName()));
            player.setAvatarUrl(defaultString(request.getAvatarUrl(), player.getAvatarUrl()));
            player.setLastLoginAt(now);
            player.setUpdatedAt(now);
            playerMapper.updateById(player);
            farmService.initDefaultLands(player.getId());
        }

        return new LoginResponse(buildDevToken(player.getId()), player.getId(), newPlayer);
    }

    /**
     * 查询玩家完整档案。
     *
     * <p>当前返回玩家基础信息和土地摘要，后续会继续补仓库、升级、成就等内容。</p>
     *
     * @param playerId 玩家ID
     * @return 玩家档案响应
     */
    public PlayerProfileResponse getProfile(Long playerId) {
        Player player = playerMapper.selectById(playerId);
        if (player == null || Integer.valueOf(1).equals(player.getDeleted())) {
            throw new BizException(ErrorCode.PLAYER_NOT_FOUND);
        }

        PlayerProfileResponse response = new PlayerProfileResponse();
        response.setPlayerId(player.getId());
        response.setNickName(player.getNickName());
        response.setAvatarUrl(player.getAvatarUrl());
        response.setLevel(player.getLevel());
        response.setExp(player.getExp());
        response.setCoins(player.getCoins());
        response.setTotalIncome(player.getTotalIncome());
        response.setDailyIncome(player.getDailyIncome());
        response.setLands(toLandSummaries(farmService.listPlayerLands(playerId)));
        return response;
    }

    private Player createPlayer(String openId, LoginRequest request, LocalDateTime now) {
        Player player = new Player();
        player.setId(IdWorker.getId());
        player.setOpenId(openId);
        player.setNickName(defaultString(request.getNickName(), "农场主"));
        player.setAvatarUrl(defaultString(request.getAvatarUrl(), ""));
        player.setLevel(1);
        player.setExp(0L);
        player.setCoins(0L);
        player.setTotalIncome(0L);
        player.setDailyIncome(0L);
        player.setLastLoginAt(now);
        player.setCreatedAt(now);
        player.setUpdatedAt(now);
        player.setDeleted(0);
        return player;
    }

    private List<PlayerProfileResponse.LandSummary> toLandSummaries(List<PlayerLand> lands) {
        List<PlayerProfileResponse.LandSummary> summaries = new ArrayList<>();
        for (PlayerLand land : lands) {
            PlayerProfileResponse.LandSummary summary = new PlayerProfileResponse.LandSummary();
            summary.setLandId(land.getId());
            summary.setLandIndex(land.getLandIndex());
            summary.setStatus(land.getStatus());
            summary.setCropId(land.getCropId());
            summary.setMatureAt(land.getMatureAt() == null ? null : land.getMatureAt().toString());
            summary.setUnlocked(Integer.valueOf(1).equals(land.getUnlocked()));
            summaries.add(summary);
        }
        return summaries;
    }

    private String normalizeDevOpenId(String code) {
        return "dev_" + code.trim();
    }

    private String buildDevToken(Long playerId) {
        return "dev-token-" + playerId;
    }

    private String defaultString(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
