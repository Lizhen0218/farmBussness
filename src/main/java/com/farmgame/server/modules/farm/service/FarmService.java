package com.farmgame.server.modules.farm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.farmgame.server.common.api.ErrorCode;
import com.farmgame.server.common.exception.BizException;
import com.farmgame.server.modules.farm.dto.FarmStateResponse;
import com.farmgame.server.modules.farm.dto.HarvestRequest;
import com.farmgame.server.modules.farm.dto.HarvestResponse;
import com.farmgame.server.modules.farm.dto.PlantRequest;
import com.farmgame.server.modules.farm.dto.PlantResponse;
import com.farmgame.server.modules.farm.entity.PlayerLand;
import com.farmgame.server.modules.farm.mapper.PlayerLandMapper;
import com.farmgame.server.modules.gameconfig.model.CropConfig;
import com.farmgame.server.modules.gameconfig.service.GameConfigService;
import com.farmgame.server.modules.inventory.entity.PlayerInventory;
import com.farmgame.server.modules.inventory.mapper.PlayerInventoryMapper;
import com.farmgame.server.modules.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FarmService {

    public static final int MVP_INITIAL_LAND_COUNT = 6;
    public static final String LAND_STATUS_EMPTY = "EMPTY";
    public static final String LAND_STATUS_GROWING = "GROWING";

    private final PlayerLandMapper playerLandMapper;
    private final PlayerInventoryMapper playerInventoryMapper;
    private final InventoryService inventoryService;
    private final GameConfigService gameConfigService;

    public FarmService(PlayerLandMapper playerLandMapper,
                       PlayerInventoryMapper playerInventoryMapper,
                       InventoryService inventoryService,
                       GameConfigService gameConfigService) {
        this.playerLandMapper = playerLandMapper;
        this.playerInventoryMapper = playerInventoryMapper;
        this.inventoryService = inventoryService;
        this.gameConfigService = gameConfigService;
    }

    /**
     * 为新玩家初始化 MVP 默认土地。
     *
     * <p>若玩家已经存在土地，则直接返回，保证重复登录时不会重复创建土地。</p>
     *
     * @param playerId 玩家ID
     */
    public void initDefaultLands(Long playerId) {
        long count = playerLandMapper.selectCount(new LambdaQueryWrapper<PlayerLand>()
                .eq(PlayerLand::getPlayerId, playerId)
                .eq(PlayerLand::getDeleted, 0));
        if (count > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        for (int index = 1; index <= MVP_INITIAL_LAND_COUNT; index++) {
            PlayerLand land = new PlayerLand();
            land.setId(IdWorker.getId());
            land.setPlayerId(playerId);
            land.setLandIndex(index);
            land.setStatus(LAND_STATUS_EMPTY);
            land.setUnlocked(1);
            land.setCreatedAt(now);
            land.setUpdatedAt(now);
            land.setDeleted(0);
            playerLandMapper.insert(land);
        }
    }

    public List<PlayerLand> listPlayerLands(Long playerId) {
        return playerLandMapper.selectList(new LambdaQueryWrapper<PlayerLand>()
                .eq(PlayerLand::getPlayerId, playerId)
                .eq(PlayerLand::getDeleted, 0)
                .orderByAsc(PlayerLand::getLandIndex));
    }

    public FarmStateResponse getFarmState(Long playerId) {
        LocalDateTime now = LocalDateTime.now();
        FarmStateResponse response = new FarmStateResponse();
        response.setPlayerId(playerId);
        response.setServerTime(System.currentTimeMillis());
        response.setLands(toLandStates(listPlayerLands(playerId), now));
        return response;
    }

    /**
     * 播种作物。
     *
     * <p>核心校验包括：作物配置存在、土地属于当前玩家、土地已解锁、土地为空。
     * 播种成功后会写入作物ID、播种时间和成熟时间。</p>
     *
     * @param playerId 玩家ID
     * @param request 播种请求
     * @return 播种结果
     */
    @Transactional(rollbackFor = Exception.class)
    public PlantResponse plant(Long playerId, PlantRequest request) {
        CropConfig crop = gameConfigService.getCrop(request.getCropId());
        PlayerLand land = getOwnedLand(playerId, request.getLandId());
        if (!Integer.valueOf(1).equals(land.getUnlocked())) {
            throw new BizException(ErrorCode.FORBIDDEN, "土地尚未解锁");
        }
        if (!LAND_STATUS_EMPTY.equals(land.getStatus())) {
            throw new BizException(ErrorCode.LAND_NOT_EMPTY);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime matureAt = now.plusSeconds(crop.getGrowTimeSeconds());
        land.setStatus(LAND_STATUS_GROWING);
        land.setCropId(crop.getCropId());
        land.setPlantedAt(now);
        land.setMatureAt(matureAt);
        land.setUpdatedAt(now);
        playerLandMapper.updateById(land);

        PlantResponse response = new PlantResponse();
        response.setLandId(land.getId());
        response.setCropId(crop.getCropId());
        response.setPlantedAt(now.toString());
        response.setMatureAt(matureAt.toString());
        return response;
    }

    /**
     * 收获成熟作物。
     *
     * <p>核心校验包括：土地属于当前玩家、土地处于种植中、作物已经成熟。
     * 收获成功后作物进入仓库，土地重置为空地。</p>
     *
     * @param playerId 玩家ID
     * @param request 收获请求
     * @return 收获结果
     */
    @Transactional(rollbackFor = Exception.class)
    public HarvestResponse harvest(Long playerId, HarvestRequest request) {
        PlayerLand land = getOwnedLand(playerId, request.getLandId());
        LocalDateTime now = LocalDateTime.now();
        if (!LAND_STATUS_GROWING.equals(land.getStatus()) || land.getCropId() == null) {
            throw new BizException(ErrorCode.BAD_REQUEST, "土地上没有可收获作物");
        }
        if (!isMature(land, now)) {
            throw new BizException(ErrorCode.CROP_NOT_MATURE);
        }

        CropConfig crop = gameConfigService.getCrop(land.getCropId());
        inventoryService.addItem(playerId, crop.getCropId(), InventoryService.ITEM_TYPE_CROP, 1);
        Long inventoryQuantity = getInventoryQuantity(playerId, crop.getCropId());

        land.setStatus(LAND_STATUS_EMPTY);
        land.setCropId(null);
        land.setPlantedAt(null);
        land.setMatureAt(null);
        land.setUpdatedAt(now);
        playerLandMapper.updateById(land);

        HarvestResponse response = new HarvestResponse();
        response.setLandId(land.getId());
        response.setItemId(crop.getCropId());
        response.setItemName(crop.getName());
        response.setCount(1);
        response.setInventoryQuantity(inventoryQuantity);
        return response;
    }

    public List<FarmStateResponse.LandState> toLandStates(List<PlayerLand> lands, LocalDateTime now) {
        List<FarmStateResponse.LandState> states = new ArrayList<>();
        for (PlayerLand land : lands) {
            FarmStateResponse.LandState state = new FarmStateResponse.LandState();
            state.setLandId(land.getId());
            state.setLandIndex(land.getLandIndex());
            state.setStatus(land.getStatus());
            state.setCropId(land.getCropId());
            state.setPlantedAt(format(land.getPlantedAt()));
            state.setMatureAt(format(land.getMatureAt()));
            state.setUnlocked(Integer.valueOf(1).equals(land.getUnlocked()));
            state.setMature(isMature(land, now));
            states.add(state);
        }
        return states;
    }

    private boolean isMature(PlayerLand land, LocalDateTime now) {
        return LAND_STATUS_GROWING.equals(land.getStatus())
                && land.getMatureAt() != null
                && !land.getMatureAt().isAfter(now);
    }

    private PlayerLand getOwnedLand(Long playerId, Long landId) {
        PlayerLand land = playerLandMapper.selectById(landId);
        if (land == null || Integer.valueOf(1).equals(land.getDeleted()) || !playerId.equals(land.getPlayerId())) {
            throw new BizException(ErrorCode.LAND_NOT_FOUND);
        }
        return land;
    }

    private Long getInventoryQuantity(Long playerId, String itemId) {
        PlayerInventory inventory = playerInventoryMapper.selectOne(new LambdaQueryWrapper<PlayerInventory>()
                .eq(PlayerInventory::getPlayerId, playerId)
                .eq(PlayerInventory::getItemId, itemId)
                .eq(PlayerInventory::getDeleted, 0)
                .last("LIMIT 1"));
        return inventory == null ? 0L : inventory.getQuantity();
    }

    private String format(LocalDateTime time) {
        return time == null ? null : time.toString();
    }
}
