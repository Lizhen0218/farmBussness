package com.farmgame.server.modules.inventory;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.inventory.dto.InventoryItemResponse;
import com.farmgame.server.modules.inventory.dto.SellItemRequest;
import com.farmgame.server.modules.inventory.dto.SellItemResponse;
import com.farmgame.server.modules.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<SellItemResponse> sell(@RequestHeader("X-Player-Id") Long playerId,
                                              @Valid @RequestBody SellItemRequest request) {
        return ApiResponse.success(inventoryService.sellItem(playerId, request));
    }
}
