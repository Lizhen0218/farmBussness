package com.farmgame.server.modules.user;

import com.farmgame.server.common.api.ApiResponse;
import com.farmgame.server.modules.user.dto.LoginRequest;
import com.farmgame.server.modules.user.dto.LoginResponse;
import com.farmgame.server.modules.user.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final PlayerService playerService;

    public UserController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "微信小游戏登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(playerService.login(request));
    }
}
