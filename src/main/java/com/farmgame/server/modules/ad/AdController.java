package com.farmgame.server.modules.ad;

import com.farmgame.server.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "广告奖励")
@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Operation(summary = "领取广告奖励")
    @PostMapping("/reward")
    public ApiResponse<Map<String, Object>> reward(@RequestBody AdRewardRequest request) {
        return ApiResponse.success(Map.of("rewardType", request.getRewardType(), "claimed", false));
    }

    public static class AdRewardRequest {
        @NotBlank
        private String rewardType;
        @NotBlank
        private String clientRequestId;

        public String getRewardType() {
            return rewardType;
        }

        public void setRewardType(String rewardType) {
            this.rewardType = rewardType;
        }

        public String getClientRequestId() {
            return clientRequestId;
        }

        public void setClientRequestId(String clientRequestId) {
            this.clientRequestId = clientRequestId;
        }
    }
}
