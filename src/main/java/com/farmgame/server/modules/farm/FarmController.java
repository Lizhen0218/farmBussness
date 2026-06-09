package com.farmgame.server.modules.farm;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.farm.dto.FarmStateResponse;
import com.farmgame.server.modules.farm.dto.HarvestRequest;
import com.farmgame.server.modules.farm.dto.HarvestResponse;
import com.farmgame.server.modules.farm.dto.PlantRequest;
import com.farmgame.server.modules.farm.dto.PlantResponse;
import com.farmgame.server.modules.farm.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "农场")
@RestController
@RequestMapping("/api/farm")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @Operation(summary = "获取农场状态")
    @GetMapping("/state")
    public ApiResponse<FarmStateResponse> state(@RequestHeader("X-Player-Id") Long playerId) {
        return ApiResponse.success(farmService.getFarmState(playerId));
    }

    @Operation(summary = "播种")
    @PostMapping("/plant")
    public ApiResponse<PlantResponse> plant(@RequestHeader("X-Player-Id") Long playerId,
                                            @Valid @RequestBody PlantRequest request) {
        return ApiResponse.success(farmService.plant(playerId, request));
    }

    @Operation(summary = "收获")
    @PostMapping("/harvest")
    public ApiResponse<HarvestResponse> harvest(@RequestHeader("X-Player-Id") Long playerId,
                                                @Valid @RequestBody HarvestRequest request) {
        return ApiResponse.success(farmService.harvest(playerId, request));
    }
}
