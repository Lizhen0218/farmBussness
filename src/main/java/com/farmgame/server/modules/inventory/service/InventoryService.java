package com.farmgame.server.modules.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.farmgame.server.common.api.ErrorCode;
import com.farmgame.server.common.exception.BizException;
import com.farmgame.server.modules.economy.entity.EconomyLog;
import com.farmgame.server.modules.economy.mapper.EconomyLogMapper;
import com.farmgame.server.modules.gameconfig.model.CropConfig;
import com.farmgame.server.modules.gameconfig.service.GameConfigService;
import com.farmgame.server.modules.inventory.dto.SellItemRequest;
import com.farmgame.server.modules.inventory.dto.SellItemResponse;
import com.farmgame.server.modules.inventory.entity.PlayerInventory;
import com.farmgame.server.modules.inventory.dto.InventoryItemResponse;
import com.farmgame.server.modules.inventory.mapper.PlayerInventoryMapper;
import com.farmgame.server.modules.user.entity.Player;
import com.farmgame.server.modules.user.mapper.PlayerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class InventoryService {

    public static final String ITEM_TYPE_CROP = "CROP";
    private static final String CHANGE_TYPE_ADD = "ADD";
    private static final String BIZ_TYPE_INVENTORY_SELL = "INVENTORY_SELL";

    private final PlayerInventoryMapper playerInventoryMapper;
    private final PlayerMapper playerMapper;
    private final EconomyLogMapper economyLogMapper;
    private final GameConfigService gameConfigService;

    public InventoryService(PlayerInventoryMapper playerInventoryMapper,
                            PlayerMapper playerMapper,
                            EconomyLogMapper economyLogMapper,
                            GameConfigService gameConfigService) {
        this.playerInventoryMapper = playerInventoryMapper;
        this.playerMapper = playerMapper;
        this.economyLogMapper = economyLogMapper;
        this.gameConfigService = gameConfigService;
    }

    /**
     * 增加玩家仓库物品数量。
     *
     * <p>若该玩家尚无对应物品记录则创建新记录，否则在原数量上累加。</p>
     *
     * @param playerId 玩家ID
     * @param itemId 物品ID
     * @param itemType 物品类型
     * @param count 增加数量
     */
    public void addItem(Long playerId, String itemId, String itemType, long count) {
        LocalDateTime now = LocalDateTime.now();
        PlayerInventory inventory = playerInventoryMapper.selectOne(new LambdaQueryWrapper<PlayerInventory>()
                .eq(PlayerInventory::getPlayerId, playerId)
                .eq(PlayerInventory::getItemId, itemId)
                .eq(PlayerInventory::getDeleted, 0)
                .last("LIMIT 1"));
        if (inventory == null) {
            inventory = new PlayerInventory();
            inventory.setId(IdWorker.getId());
            inventory.setPlayerId(playerId);
            inventory.setItemId(itemId);
            inventory.setItemType(itemType);
            inventory.setQuantity(count);
            inventory.setCreatedAt(now);
            inventory.setUpdatedAt(now);
            inventory.setDeleted(0);
            playerInventoryMapper.insert(inventory);
            return;
        }

        inventory.setQuantity(inventory.getQuantity() + count);
        inventory.setUpdatedAt(now);
        playerInventoryMapper.updateById(inventory);
    }

    public List<PlayerInventory> listItems(Long playerId) {
        return playerInventoryMapper.selectList(new LambdaQueryWrapper<PlayerInventory>()
                .eq(PlayerInventory::getPlayerId, playerId)
                .eq(PlayerInventory::getDeleted, 0)
                .gt(PlayerInventory::getQuantity, 0)
                .orderByAsc(PlayerInventory::getItemType)
                .orderByAsc(PlayerInventory::getItemId));
    }

    /**
     * 查询玩家仓库并转换为接口响应对象。
     *
     * @param playerId 玩家ID
     * @return 仓库物品列表
     */
    public List<InventoryItemResponse> listItemResponses(Long playerId) {
        List<InventoryItemResponse> responses = new ArrayList<>();
        for (PlayerInventory item : listItems(playerId)) {
            InventoryItemResponse response = new InventoryItemResponse();
            response.setItemId(item.getItemId());
            response.setItemType(item.getItemType());
            response.setQuantity(item.getQuantity());
            responses.add(response);
        }
        return responses;
    }

    /**
     * 直接出售仓库中的作物，并给玩家增加金币。
     *
     * <p>当前 MVP 只允许出售作物。售价来自作物配置，库存扣减、玩家金币更新和金币流水记录在同一事务中完成。</p>
     *
     * @param playerId 玩家ID
     * @param request 售出请求
     * @return 售出结果
     */
    @Transactional(rollbackFor = Exception.class)
    public SellItemResponse sellItem(Long playerId, SellItemRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Player player = playerMapper.selectById(playerId);
        if (player == null || Integer.valueOf(1).equals(player.getDeleted())) {
            throw new BizException(ErrorCode.PLAYER_NOT_FOUND);
        }

        CropConfig crop = gameConfigService.getCrop(request.getItemId());
        PlayerInventory inventory = playerInventoryMapper.selectOne(new LambdaQueryWrapper<PlayerInventory>()
                .eq(PlayerInventory::getPlayerId, playerId)
                .eq(PlayerInventory::getItemId, request.getItemId())
                .eq(PlayerInventory::getItemType, ITEM_TYPE_CROP)
                .eq(PlayerInventory::getDeleted, 0)
                .last("LIMIT 1"));
        if (inventory == null || inventory.getQuantity() == null || inventory.getQuantity() < request.getCount()) {
            throw new BizException(ErrorCode.INVENTORY_NOT_ENOUGH);
        }

        long sellCount = request.getCount();
        long remainingQuantity = inventory.getQuantity() - sellCount;
        if (remainingQuantity == 0) {
            inventory.setQuantity(0L);
            inventory.setDeleted(1);
        } else {
            inventory.setQuantity(remainingQuantity);
        }
        inventory.setUpdatedAt(now);
        playerInventoryMapper.updateById(inventory);

        long beforeCoins = player.getCoins() == null ? 0L : player.getCoins();
        long coinsAdded = sellCount * crop.getSellPrice();
        long afterCoins = beforeCoins + coinsAdded;
        player.setCoins(afterCoins);
        player.setTotalIncome((player.getTotalIncome() == null ? 0L : player.getTotalIncome()) + coinsAdded);
        player.setDailyIncome((player.getDailyIncome() == null ? 0L : player.getDailyIncome()) + coinsAdded);
        player.setUpdatedAt(now);
        playerMapper.updateById(player);

        EconomyLog log = new EconomyLog();
        log.setId(IdWorker.getId());
        log.setPlayerId(playerId);
        log.setChangeType(CHANGE_TYPE_ADD);
        log.setAmount(coinsAdded);
        log.setBeforeAmount(beforeCoins);
        log.setAfterAmount(afterCoins);
        log.setBizType(BIZ_TYPE_INVENTORY_SELL);
        log.setBizId(request.getItemId() + ":" + log.getId());
        log.setRemark("直接售出" + crop.getName() + "x" + sellCount);
        log.setCreatedAt(now);
        economyLogMapper.insert(log);

        SellItemResponse response = new SellItemResponse();
        response.setItemId(request.getItemId());
        response.setCount(request.getCount());
        response.setUnitPrice(crop.getSellPrice().longValue());
        response.setCoinsAdded(coinsAdded);
        response.setRemainingQuantity(remainingQuantity);
        response.setCoinsBalance(afterCoins);
        return response;
    }
}
