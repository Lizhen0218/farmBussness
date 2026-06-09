package com.farmgame.server.modules.inventory;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.inventory.dto.InventoryItemResponse;
import com.farmgame.server.modules.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "仓库")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "获取仓库列表")
    @GetMapping("/list")
    public ApiResponse<List<InventoryItemResponse>> list(@RequestHeader("X-Player-Id") Long playerId) {
        return ApiResponse.success(inventoryService.listItemResponses(playerId));
    }

    @Operation(summary = "直接出售物品")
    @PostMapping("/sell")
    public ApiResponse<Map<String, Object>> sell(@RequestBody SellItemRequest request) {
        return ApiResponse.success(Map.of("coinsAdded", 0, "itemId", request.getItemId()));
    }

    public static class SellItemRequest {
        @NotBlank
        private String itemId;
        @Min(1)
        private int count;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
