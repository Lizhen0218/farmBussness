package com.farmgame.server.modules.user;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.user.dto.PlayerProfileResponse;
import com.farmgame.server.modules.user.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "玩家")
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "获取玩家完整数据")
    @GetMapping("/profile")
    public ApiResponse<PlayerProfileResponse> profile(@RequestHeader("X-Player-Id") Long playerId) {
        return ApiResponse.success(playerService.getProfile(playerId));
    }
}
