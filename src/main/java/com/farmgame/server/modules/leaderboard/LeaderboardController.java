package com.farmgame.server.modules.leaderboard;

import com.farmgame.server.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "排行榜")
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Operation(summary = "获取金币收入排行榜")
    @GetMapping("/income")
    public ApiResponse<List<Map<String, Object>>> income(@RequestParam(defaultValue = "daily") String type) {
        return ApiResponse.success(List.of());
    }
}
