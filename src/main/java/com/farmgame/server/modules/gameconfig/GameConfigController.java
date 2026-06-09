package com.farmgame.server.modules.gameconfig;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.gameconfig.model.CropConfig;
import com.farmgame.server.modules.gameconfig.service.GameConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@Tag(name = "游戏配置")
@RestController
@RequestMapping("/api/config")
public class GameConfigController {

    private final GameConfigService gameConfigService;

    public GameConfigController(GameConfigService gameConfigService) {
        this.gameConfigService = gameConfigService;
    }

    @Operation(summary = "获取配置版本")
    @GetMapping("/version")
    public ApiResponse<Map<String, Object>> version() {
        return ApiResponse.success(Map.of("version", "mvp-0.1.0"));
    }

    @Operation(summary = "获取作物配置")
    @GetMapping("/crops")
    public ApiResponse<List<CropConfig>> crops() {
        return ApiResponse.success(gameConfigService.listCrops());
    }
}
