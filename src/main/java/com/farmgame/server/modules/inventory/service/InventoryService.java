package com.farmgame.server.modules.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.farmgame.server.modules.inventory.entity.PlayerInventory;
import com.farmgame.server.modules.inventory.dto.InventoryItemResponse;
import com.farmgame.server.modules.inventory.mapper.PlayerInventoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class InventoryService {

    public static final String ITEM_TYPE_CROP = "CROP";

    private final PlayerInventoryMapper playerInventoryMapper;

    public InventoryService(PlayerInventoryMapper playerInventoryMapper) {
        this.playerInventoryMapper = playerInventoryMapper;
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
}
